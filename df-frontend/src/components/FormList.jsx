import React, { useEffect, useState, useCallback } from "react";
import { getAllForms, getPreviewPdfUrl, getDownloadPdfUrl } from "../services/api";

export default function FormList({ refreshTrigger }) {
  const [forms, setForms] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const loadForms = useCallback(async () => {
    setLoading(true);
    setError("");
    try {
      const data = await getAllForms();
      // Handle both raw list and paged response
      const list = Array.isArray(data) ? data : data.content || [];
      setForms(list);
    } catch (err) {
      console.error("Failed to load submitted forms:", err);
      setError("Unable to load submitted applications. Please check if the backend service is running.");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadForms();
  }, [loadForms, refreshTrigger]);

  return (
    <div className="max-w-4xl mx-auto my-8 p-8 bg-white rounded-xl shadow-md border border-gray-100">
      <div className="flex items-center justify-between border-b pb-4 mb-6 flex-wrap gap-4">
        <div>
          <h2 className="text-2xl font-bold text-gray-900">
            Submitted Loan Applications
          </h2>
          <p className="text-sm text-gray-500 mt-1">
            Real-time view of submitted customer applications stored in the persistence layer.
          </p>
        </div>

        <button
          onClick={loadForms}
          disabled={loading}
          className="px-4 py-2 text-xs font-semibold bg-gray-100 hover:bg-gray-200 text-gray-700 rounded transition flex items-center gap-1.5 disabled:opacity-50"
        >
          <svg className={`w-3.5 h-3.5 ${loading ? "animate-spin" : ""}`} fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
          </svg>
          Refresh List
        </button>
      </div>

      {loading && forms.length === 0 && (
        <div className="text-center py-12 text-gray-500">
          <svg className="animate-spin h-8 w-8 mx-auto text-blue-600 mb-3" viewBox="0 0 24 24">
            <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" fill="none" />
            <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8H4z" />
          </svg>
          <p className="text-sm">Loading applications...</p>
        </div>
      )}

      {error && (
        <div className="p-4 bg-red-50 border border-red-200 text-red-700 rounded-lg text-sm flex justify-between items-center">
          <p>{error}</p>
          <button onClick={loadForms} className="underline font-semibold ml-2">Retry</button>
        </div>
      )}

      {!loading && !error && forms.length === 0 && (
        <div className="text-center py-12 border-2 border-dashed border-gray-200 rounded-lg">
          <p className="text-gray-500 font-medium text-base">No loan applications submitted yet.</p>
          <p className="text-gray-400 text-xs mt-1">Submit your first application using the form above to generate and download documents.</p>
        </div>
      )}

      <div className="space-y-4">
        {forms.map((item) => (
          <div
            key={item.id}
            className="border border-gray-200 rounded-lg p-5 hover:border-blue-300 transition-colors bg-gray-50 hover:bg-white"
          >
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
              <div className="space-y-1">
                <div className="flex items-center gap-2">
                  <span className="text-xs font-bold px-2 py-0.5 bg-blue-100 text-blue-800 rounded">
                    #{item.id}
                  </span>
                  <h3 className="font-bold text-gray-900 text-base">{item.name}</h3>
                </div>

                <p className="text-sm font-semibold text-gray-700">{item.subject}</p>

                <p className="text-xs text-gray-500">
                  <span className="font-medium text-gray-600">Email:</span> {item.email} |{" "}
                  <span className="font-medium text-gray-600">Contact:</span> {item.contactNumber} |{" "}
                  <span className="font-medium text-gray-600">Location:</span> {item.city}, {item.state} ({item.pinCode})
                </p>

                <p className="text-xs text-gray-400">
                  Application Date: {item.date} {item.createdAt ? `| Logged: ${new Date(item.createdAt).toLocaleString()}` : ""}
                </p>
              </div>

              <div className="flex items-center gap-2 flex-shrink-0">
                <a
                  href={getPreviewPdfUrl(item.id)}
                  target="_blank"
                  rel="noreferrer"
                  className="px-3.5 py-1.5 text-xs font-semibold text-emerald-700 bg-emerald-50 border border-emerald-300 rounded hover:bg-emerald-100 transition inline-flex items-center gap-1"
                >
                  <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                  </svg>
                  Preview PDF
                </a>

                <a
                  href={getDownloadPdfUrl(item.id)}
                  className="px-3.5 py-1.5 text-xs font-semibold text-white bg-blue-600 rounded hover:bg-blue-700 transition inline-flex items-center gap-1"
                >
                  <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4" />
                  </svg>
                  Download PDF
                </a>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
