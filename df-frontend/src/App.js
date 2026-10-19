import React, { useState } from "react";
import Form from "./components/Form";
import FormList from "./components/FormList";

export default function App() {
  const [refreshTrigger, setRefreshTrigger] = useState(0);

  const handleFormSubmitted = () => {
    // Increment trigger to notify FormList to reload automatically
    setRefreshTrigger((prev) => prev + 1);
  };

  return (
    <div className="min-h-screen bg-slate-50 text-slate-800">
      {/* Banking Application Header */}
      <header className="bg-slate-900 text-white shadow-lg sticky top-0 z-50">
        <div className="max-w-6xl mx-auto px-6 py-4 flex items-center justify-between">
          <div className="flex items-center space-x-3">
            <div className="p-2 bg-blue-600 rounded-lg shadow">
              <svg className="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
              </svg>
            </div>
            <div>
              <h1 className="text-xl font-bold tracking-tight">
                Banking Loan Form Management
              </h1>
              <p className="text-xs text-slate-400">
                Automated Customer Form Verification & Document Processing System
              </p>
            </div>
          </div>

          <div className="flex items-center space-x-4">
            <span className="hidden sm:inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-emerald-900 text-emerald-300 border border-emerald-700">
              ● System Online
            </span>
            <a
              href="http://localhost:8080/swagger-ui.html"
              target="_blank"
              rel="noreferrer"
              className="text-xs font-semibold text-slate-300 hover:text-white underline"
            >
              API Docs (Swagger)
            </a>
          </div>
        </div>
      </header>

      {/* Main Content Area */}
      <main className="max-w-6xl mx-auto px-4 py-8">
        <Form onSubmitted={handleFormSubmitted} />
        <FormList refreshTrigger={refreshTrigger} />
      </main>

      {/* Footer */}
      <footer className="border-t border-slate-200 mt-16 py-6 text-center text-xs text-slate-500">
        <p>Banking Loan Form Management and Document Processing System &copy; 2026. All rights reserved.</p>
        <p className="mt-1">Powered by Spring Boot 3 & React</p>
      </footer>
    </div>
  );
}
