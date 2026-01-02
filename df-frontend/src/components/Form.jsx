// import { useState } from "react";
// import { submitForm } from "../services/api";

// const Form = ({ refresh }) => {
//   const [form, setForm] = useState({
//     name: "",
//     email: "",
//     phone: "",
//     address: "",
//     subject: "",
//     message: "",
//   });

//   const handleChange = (e) =>
//     setForm({ ...form, [e.target.name]: e.target.value });

//   const handleSubmit = async (e) => {
//     e.preventDefault();
//     await submitForm(form);
//     alert("Form submitted successfully!");
//     setForm({
//       name: "",
//       email: "",
//       phone: "",
//       address: "",
//       subject: "",
//       message: "",
//     });
//     refresh();
//   };

//   return (
//     <div className="max-w-2xl mx-auto bg-white p-8 rounded-xl shadow-lg">
//       <h2 className="text-2xl font-bold text-center text-blue-600 mb-6">
//         Contact Form
//       </h2>

//       <form onSubmit={handleSubmit} className="space-y-4">
//         {["name", "email", "phone", "address", "subject"].map((field) => (
//           <input
//             key={field}
//             name={field}
//             placeholder={field.toUpperCase()}
//             value={form[field]}
//             onChange={handleChange}
//             className="w-full p-3 border rounded-lg focus:ring-2 focus:ring-blue-500"
//             required
//           />
//         ))}

//         <textarea
//           name="message"
//           placeholder="MESSAGE"
//           value={form.message}
//           onChange={handleChange}
//           rows="4"
//           className="w-full p-3 border rounded-lg focus:ring-2 focus:ring-blue-500"
//           required
//         />

//         <button className="w-full bg-blue-600 text-white py-3 rounded-lg hover:bg-blue-700">
//           Submit
//         </button>
//       </form>
//     </div>
//   );
// };

// export default Form;





















import { useState } from "react";
import api from "../services/api";

export default function Form() {

  const [form, setForm] = useState({
    name: "", email: "", contactNumber: "",
    address: "", city: "", state: "", pinCode: "",
    date: "", subject: "", message: ""
  });

  const handleChange = e =>
    setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async e => {
    e.preventDefault();
    await api.post("", form);
    alert("Application submitted successfully");
  };

  return (
    <div className="max-w-3xl mx-auto p-6 bg-white shadow-lg">
      <h1 className="text-2xl font-bold text-center mb-6">
        Official Application Form
      </h1>

      <form onSubmit={handleSubmit} className="space-y-4">

        {Object.keys(form).map(key => (
          <input
            key={key}
            name={key}
            placeholder={key.toUpperCase()}
            value={form[key]}
            onChange={handleChange}
            className="w-full border p-2 rounded"
            type={key === "date" ? "date" : "text"}
          />
        ))}

        <button className="bg-blue-600 text-white px-6 py-2 rounded">
          Submit Application
        </button>
      </form>
    </div>
  );
}
