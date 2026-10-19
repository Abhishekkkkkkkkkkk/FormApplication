import axios from "axios";

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || "http://localhost:8080";

const api = axios.create({
  baseURL: `${API_BASE_URL}/api/forms`,
  headers: {
    "Content-Type": "application/json",
    Accept: "application/json",
  },
  timeout: 15000,
});

export const submitForm = async (formData) => {
  const response = await api.post("", formData);
  return response.data;
};

export const getAllForms = async (page, size) => {
  const params = {};
  if (page !== undefined && size !== undefined) {
    params.page = page;
    params.size = size;
  }
  const response = await api.get("", { params });
  return response.data;
};

export const getFormById = async (id) => {
  const response = await api.get(`/${id}`);
  return response.data;
};

export const getPreviewPdfUrl = (id) => {
  return `${API_BASE_URL}/api/forms/pdf/preview/${id}`;
};

export const getDownloadPdfUrl = (id) => {
  return `${API_BASE_URL}/api/forms/pdf/download/${id}`;
};

export default api;
