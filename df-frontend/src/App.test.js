import { render, screen, waitFor } from '@testing-library/react';
import App from './App';
import * as api from './services/api';

jest.mock('./services/api');

beforeEach(() => {
  api.getAllForms.mockResolvedValue([]);
  api.getPreviewPdfUrl.mockImplementation((id) => `http://localhost:8080/api/forms/pdf/preview/${id}`);
  api.getDownloadPdfUrl.mockImplementation((id) => `http://localhost:8080/api/forms/pdf/download/${id}`);
});

test('renders Banking Loan Form Management header', async () => {
  render(<App />);
  const headerElement = screen.getByRole('heading', { name: /Banking Loan Form Management/i });
  expect(headerElement).toBeInTheDocument();
});

test('renders form section and submitted applications section', async () => {
  render(<App />);
  expect(screen.getByText(/Official Banking Loan Application Form/i)).toBeInTheDocument();
  expect(screen.getByText(/Submitted Loan Applications/i)).toBeInTheDocument();
  await waitFor(() => {
    expect(screen.getByText(/No loan applications submitted yet/i)).toBeInTheDocument();
  });
});
