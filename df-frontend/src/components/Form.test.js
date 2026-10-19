import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import Form from './Form';

test('renders all mandatory form inputs and labels', () => {
  render(<Form onSubmitted={() => {}} />);

  expect(screen.getByLabelText(/Full Name/i)).toBeInTheDocument();
  expect(screen.getByLabelText(/Email Address/i)).toBeInTheDocument();
  expect(screen.getByLabelText(/Contact Number/i)).toBeInTheDocument();
  expect(screen.getByLabelText(/Street Address/i)).toBeInTheDocument();
  expect(screen.getByLabelText(/^City/i)).toBeInTheDocument();
  expect(screen.getByRole('textbox', { name: /^State/i })).toBeInTheDocument();
  expect(screen.getByLabelText(/PIN Code/i)).toBeInTheDocument();
  expect(screen.getByLabelText(/Application Date/i)).toBeInTheDocument();
  expect(screen.getByLabelText(/^Subject/i)).toBeInTheDocument();
  expect(screen.getByLabelText(/Detailed Message/i)).toBeInTheDocument();
  expect(screen.getByRole('button', { name: /Submit Application/i })).toBeInTheDocument();
});

test('shows validation errors when submitting empty form', async () => {
  render(<Form onSubmitted={() => {}} />);

  const submitButton = screen.getByRole('button', { name: /Submit Application/i });
  fireEvent.click(submitButton);

  await waitFor(() => {
    expect(screen.getByText(/Name is required/i)).toBeInTheDocument();
    expect(screen.getByText(/Email is required/i)).toBeInTheDocument();
    expect(screen.getByText(/Contact number is required/i)).toBeInTheDocument();
    expect(screen.getByText(/Address is required/i)).toBeInTheDocument();
    expect(screen.getByText(/City is required/i)).toBeInTheDocument();
    expect(screen.getByText(/State is required/i)).toBeInTheDocument();
    expect(screen.getByText(/PIN code is required/i)).toBeInTheDocument();
    expect(screen.getByText(/Subject is required/i)).toBeInTheDocument();
    expect(screen.getByText(/Message body is required/i)).toBeInTheDocument();
  });
});

test('shows error when contact number is not 10 digits', async () => {
  render(<Form onSubmitted={() => {}} />);

  const phoneInput = screen.getByLabelText(/Contact Number/i);
  fireEvent.change(phoneInput, { target: { value: '12345' } });

  const submitButton = screen.getByRole('button', { name: /Submit Application/i });
  fireEvent.click(submitButton);

  await waitFor(() => {
    expect(screen.getByText(/Contact number must be exactly 10 digits/i)).toBeInTheDocument();
  });
});
