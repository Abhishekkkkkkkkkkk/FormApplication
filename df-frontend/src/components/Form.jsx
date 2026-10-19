import React, { useState } from "react";
import { submitForm, getPreviewPdfUrl, getDownloadPdfUrl } from "../services/api";

const INITIAL_FORM = {
  name: "",
  email: "",
  contactNumber: "",
  address: "",
  city: "",
  state: "",
  pinCode: "",
  date: new Date().toISOString().split("T")[0],
  subject: "",
  message: "",
};

export default function Form({ onSubmitted }) {
  const [form, setForm] = useState(INITIAL_FORM);
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);
  const [submittedApp, setSubmittedApp] = useState(null);
  const [serverError, setServerError] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
    // Clear field-specific error as user types
    if (errors[name]) {
      setErrors((prev) => {
        const updated = { ...prev };
        delete updated[name];
        return updated;
      });
    }
  };

  const validateClientSide = () => {
    const newErrors = {};
    if (!form.name.trim()) newErrors.name = "Name is required";
    if (!form.email.trim()) {
      newErrors.email = "Email is required";
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email.trim())) {
      newErrors.email = "Please enter a valid email address";
    }
    if (!form.contactNumber.trim()) {
      newErrors.contactNumber = "Contact number is required";
    } else if (!/^\d{10}$/.test(form.contactNumber.trim())) {
      newErrors.contactNumber = "Contact number must be exactly 10 digits";
    }
    if (!form.address.trim()) newErrors.address = "Address is required";
    if (!form.city.trim()) newErrors.city = "City is required";
    if (!form.state.trim()) newErrors.state = "State is required";
    if (!form.pinCode.trim()) {
      newErrors.pinCode = "PIN code is required";
    } else if (!/^\d{6}$/.test(form.pinCode.trim())) {
      newErrors.pinCode = "PIN code must be exactly 6 digits";
    }
    if (!form.date) newErrors.date = "Application date is required";
    if (!form.subject.trim()) newErrors.subject = "Subject is required";
    if (!form.message.trim()) {
      newErrors.message = "Message body is required";
    } else if (form.message.trim().length < 10) {
      newErrors.message = "Message must be at least 10 characters";
    }
    return newErrors;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setServerError("");
    setSubmittedApp(null);

    const clientErrors = validateClientSide();
    if (Object.keys(clientErrors).length > 0) {
      setErrors(clientErrors);
      return;
    }

    setLoading(true);
    try {
      const result = await submitForm(form);
      setSubmittedApp(result);
      setForm(INITIAL_FORM);
      setErrors({});
      if (typeof onSubmitted === "function") {
        onSubmitted(result);
      }
    } catch (err) {
      console.error("Submission failed:", err);
      if (err.response && err.response.data) {
        const data = err.response.data;
        if (data.validationErrors) {
          setErrors(data.validationErrors);
        }
        setServerError(data.message || "Failed to submit form. Please correct highlighted errors.");
      } else {
        setServerError("Network error or server unreachable. Please check backend connection.");
      }
    } finally {
      setLoading(false);
    }
  };

  const handleReset = () => {
    setForm(INITIAL_FORM);
    setErrors({});
    setServerError("");
    setSubmittedApp(null);
  };

  return (
    <div className="max-w-4xl mx-auto my-8 p-8 bg-white rounded-xl shadow-md border border-gray-100">
      <div className="border-b pb-4 mb-6">
        <h2 className="text-2xl font-bold text-gray-900">
          Official Banking Loan Application Form
        </h2>
        <p className="text-sm text-gray-500 mt-1">
          Complete the official application details below. The backend will validate, persist, and dynamically generate an official PDF document.
        </p>
      </div>

      {serverError && (
        <div className="mb-6 p-4 bg-red-50 border-l-4 border-red-500 text-red-700 rounded-r">
          <p className="font-semibold text-sm">Submission Error</p>
          <p className="text-sm mt-0.5">{serverError}</p>
        </div>
      )}

      {submittedApp && (
        <div className="mb-6 p-5 bg-green-50 border border-green-200 rounded-lg">
          <div className="flex items-center justify-between flex-wrap gap-4">
            <div>
              <p className="text-green-800 font-bold text-base flex items-center gap-2">
                ✓ Application Submitted Successfully!
              </p>
              <p className="text-sm text-green-700 mt-1">
                Reference ID: <span className="font-semibold">#{submittedApp.id}</span> | Applicant: <span className="font-semibold">{submittedApp.name}</span>
              </p>
            </div>
            <div className="flex gap-2">
              <a
                href={getPreviewPdfUrl(submittedApp.id)}
                target="_blank"
                rel="noreferrer"
                className="px-4 py-2 text-sm font-medium bg-emerald-600 text-white rounded hover:bg-emerald-700 transition"
              >
                Preview PDF
              </a>
              <a
                href={getDownloadPdfUrl(submittedApp.id)}
                className="px-4 py-2 text-sm font-medium bg-blue-600 text-white rounded hover:bg-blue-700 transition"
              >
                Download PDF
              </a>
            </div>
          </div>
        </div>
      )}

      <form onSubmit={handleSubmit} noValidate className="space-y-6">
        {/* Section 1: Applicant Information */}
        <div>
          <h3 className="text-md font-semibold text-gray-800 mb-3 border-b pb-1 text-blue-900">
            1. Applicant Information
          </h3>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label htmlFor="name" className="block text-xs font-semibold text-gray-700 uppercase mb-1">
                Full Name <span className="text-red-500">*</span>
              </label>
              <input
                id="name"
                name="name"
                type="text"
                placeholder="e.g. John Doe"
                value={form.name}
                onChange={handleChange}
                className={`w-full p-2.5 text-sm border rounded focus:ring-2 focus:ring-blue-500 focus:outline-none ${
                  errors.name ? "border-red-500 bg-red-50" : "border-gray-300"
                }`}
              />
              {errors.name && <p className="text-red-500 text-xs mt-1">{errors.name}</p>}
            </div>

            <div>
              <label htmlFor="email" className="block text-xs font-semibold text-gray-700 uppercase mb-1">
                Email Address <span className="text-red-500">*</span>
              </label>
              <input
                id="email"
                name="email"
                type="email"
                placeholder="john.doe@example.com"
                value={form.email}
                onChange={handleChange}
                className={`w-full p-2.5 text-sm border rounded focus:ring-2 focus:ring-blue-500 focus:outline-none ${
                  errors.email ? "border-red-500 bg-red-50" : "border-gray-300"
                }`}
              />
              {errors.email && <p className="text-red-500 text-xs mt-1">{errors.email}</p>}
            </div>

            <div>
              <label htmlFor="contactNumber" className="block text-xs font-semibold text-gray-700 uppercase mb-1">
                Contact Number <span className="text-red-500">*</span>
              </label>
              <input
                id="contactNumber"
                name="contactNumber"
                type="tel"
                maxLength="10"
                placeholder="10-digit mobile number"
                value={form.contactNumber}
                onChange={handleChange}
                className={`w-full p-2.5 text-sm border rounded focus:ring-2 focus:ring-blue-500 focus:outline-none ${
                  errors.contactNumber ? "border-red-500 bg-red-50" : "border-gray-300"
                }`}
              />
              {errors.contactNumber && <p className="text-red-500 text-xs mt-1">{errors.contactNumber}</p>}
            </div>
          </div>
        </div>

        {/* Section 2: Address Details */}
        <div>
          <h3 className="text-md font-semibold text-gray-800 mb-3 border-b pb-1 text-blue-900">
            2. Residential Address
          </h3>
          <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
            <div className="md:col-span-2">
              <label htmlFor="address" className="block text-xs font-semibold text-gray-700 uppercase mb-1">
                Street Address <span className="text-red-500">*</span>
              </label>
              <input
                id="address"
                name="address"
                type="text"
                placeholder="House / Flat No., Street, Area"
                value={form.address}
                onChange={handleChange}
                className={`w-full p-2.5 text-sm border rounded focus:ring-2 focus:ring-blue-500 focus:outline-none ${
                  errors.address ? "border-red-500 bg-red-50" : "border-gray-300"
                }`}
              />
              {errors.address && <p className="text-red-500 text-xs mt-1">{errors.address}</p>}
            </div>

            <div>
              <label htmlFor="city" className="block text-xs font-semibold text-gray-700 uppercase mb-1">
                City <span className="text-red-500">*</span>
              </label>
              <input
                id="city"
                name="city"
                type="text"
                placeholder="City"
                value={form.city}
                onChange={handleChange}
                className={`w-full p-2.5 text-sm border rounded focus:ring-2 focus:ring-blue-500 focus:outline-none ${
                  errors.city ? "border-red-500 bg-red-50" : "border-gray-300"
                }`}
              />
              {errors.city && <p className="text-red-500 text-xs mt-1">{errors.city}</p>}
            </div>

            <div>
              <label htmlFor="state" className="block text-xs font-semibold text-gray-700 uppercase mb-1">
                State <span className="text-red-500">*</span>
              </label>
              <input
                id="state"
                name="state"
                type="text"
                placeholder="State"
                value={form.state}
                onChange={handleChange}
                className={`w-full p-2.5 text-sm border rounded focus:ring-2 focus:ring-blue-500 focus:outline-none ${
                  errors.state ? "border-red-500 bg-red-50" : "border-gray-300"
                }`}
              />
              {errors.state && <p className="text-red-500 text-xs mt-1">{errors.state}</p>}
            </div>

            <div>
              <label htmlFor="pinCode" className="block text-xs font-semibold text-gray-700 uppercase mb-1">
                PIN Code <span className="text-red-500">*</span>
              </label>
              <input
                id="pinCode"
                name="pinCode"
                type="text"
                maxLength="6"
                placeholder="6 digits"
                value={form.pinCode}
                onChange={handleChange}
                className={`w-full p-2.5 text-sm border rounded focus:ring-2 focus:ring-blue-500 focus:outline-none ${
                  errors.pinCode ? "border-red-500 bg-red-50" : "border-gray-300"
                }`}
              />
              {errors.pinCode && <p className="text-red-500 text-xs mt-1">{errors.pinCode}</p>}
            </div>
          </div>
        </div>

        {/* Section 3: Application Details */}
        <div>
          <h3 className="text-md font-semibold text-gray-800 mb-3 border-b pb-1 text-blue-900">
            3. Application Subject & Statement
          </h3>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-4">
            <div>
              <label htmlFor="date" className="block text-xs font-semibold text-gray-700 uppercase mb-1">
                Application Date <span className="text-red-500">*</span>
              </label>
              <input
                id="date"
                name="date"
                type="date"
                value={form.date}
                onChange={handleChange}
                className={`w-full p-2.5 text-sm border rounded focus:ring-2 focus:ring-blue-500 focus:outline-none ${
                  errors.date ? "border-red-500 bg-red-50" : "border-gray-300"
                }`}
              />
              {errors.date && <p className="text-red-500 text-xs mt-1">{errors.date}</p>}
            </div>

            <div className="md:col-span-2">
              <label htmlFor="subject" className="block text-xs font-semibold text-gray-700 uppercase mb-1">
                Subject <span className="text-red-500">*</span>
              </label>
              <input
                id="subject"
                name="subject"
                type="text"
                placeholder="e.g. Application for Home Loan Facility"
                value={form.subject}
                onChange={handleChange}
                className={`w-full p-2.5 text-sm border rounded focus:ring-2 focus:ring-blue-500 focus:outline-none ${
                  errors.subject ? "border-red-500 bg-red-50" : "border-gray-300"
                }`}
              />
              {errors.subject && <p className="text-red-500 text-xs mt-1">{errors.subject}</p>}
            </div>
          </div>

          <div>
            <div className="flex justify-between items-center mb-1">
              <label htmlFor="message" className="block text-xs font-semibold text-gray-700 uppercase">
                Detailed Message / Application Statement <span className="text-red-500">*</span>
              </label>
              <span className={`text-xs ${form.message.length < 10 ? "text-amber-600" : "text-gray-500"}`}>
                {form.message.length} characters (min 10)
              </span>
            </div>
            <textarea
              id="message"
              name="message"
              rows="4"
              placeholder="State your loan requirement, repayment details, or purpose of application in detail..."
              value={form.message}
              onChange={handleChange}
              className={`w-full p-3 text-sm border rounded focus:ring-2 focus:ring-blue-500 focus:outline-none ${
                errors.message ? "border-red-500 bg-red-50" : "border-gray-300"
              }`}
            />
            {errors.message && <p className="text-red-500 text-xs mt-1">{errors.message}</p>}
          </div>
        </div>

        {/* Buttons */}
        <div className="flex justify-end gap-3 pt-4 border-t">
          <button
            type="button"
            onClick={handleReset}
            disabled={loading}
            className="px-5 py-2.5 text-sm font-medium text-gray-700 bg-gray-100 rounded hover:bg-gray-200 transition disabled:opacity-50"
          >
            Reset Form
          </button>

          <button
            type="submit"
            disabled={loading}
            className="px-6 py-2.5 text-sm font-medium text-white bg-blue-600 rounded hover:bg-blue-700 transition disabled:opacity-50 flex items-center gap-2"
          >
            {loading ? (
              <>
                <svg className="animate-spin h-4 w-4 text-white" viewBox="0 0 24 24">
                  <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" fill="none" />
                  <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8H4z" />
                </svg>
                Submitting Application...
              </>
            ) : (
              "Submit Application"
            )}
          </button>
        </div>
      </form>
    </div>
  );
}
