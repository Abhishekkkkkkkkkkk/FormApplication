import { render, screen, waitFor } from '@testing-library/react';
import FormList from './FormList';
import * as api from '../services/api';

jest.mock('../services/api');

test('renders empty state when no forms submitted', async () => {
  api.getAllForms.mockResolvedValue([]);

  render(<FormList refreshTrigger={0} />);

  await waitFor(() => {
    expect(screen.getByText(/No loan applications submitted yet/i)).toBeInTheDocument();
  });
});

test('renders submitted forms when available', async () => {
  const mockForms = [
    {
      id: 1,
      name: 'John Doe',
      subject: 'Vehicle Loan Request',
      email: 'john@example.com',
      contactNumber: '9876543210',
      city: 'Delhi',
      state: 'Delhi',
      pinCode: '110001',
      date: '2026-10-19',
      createdAt: '2026-10-19T10:00:00',
    },
  ];

  api.getAllForms.mockResolvedValue(mockForms);
  api.getPreviewPdfUrl.mockReturnValue('http://localhost:8080/api/forms/pdf/preview/1');
  api.getDownloadPdfUrl.mockReturnValue('http://localhost:8080/api/forms/pdf/download/1');

  render(<FormList refreshTrigger={0} />);

  await waitFor(() => {
    expect(screen.getByText(/John Doe/i)).toBeInTheDocument();
    expect(screen.getByText(/Vehicle Loan Request/i)).toBeInTheDocument();
    expect(screen.getByText(/Preview PDF/i)).toBeInTheDocument();
    expect(screen.getByText(/Download PDF/i)).toBeInTheDocument();
  });
});
