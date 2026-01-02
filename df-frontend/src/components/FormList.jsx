// import { useEffect, useState } from "react";
// import { getAllForms, downloadPdf } from "../services/api";

// const FormList = () => {
//   const [data, setData] = useState([]);

//   const loadData = async () => {
//     const res = await getAllForms();
//     setData(res.data);
//   };

//   const handleDownload = async (id) => {
//     const res = await downloadPdf(id);
//     const blob = new Blob([res.data], { type: "application/pdf" });
//     const url = window.URL.createObjectURL(blob);
//     const link = document.createElement("a");
//     link.href = url;
//     link.download = `form-${id}.pdf`;
//     link.click();
//   };

//   useEffect(() => {
//     loadData();
//   }, []);

//   return (
//     <div className="max-w-4xl mx-auto mt-10">
//       <h2 className="text-xl font-semibold mb-4">Submitted Forms</h2>

//       <div className="space-y-4">
//         {data.map((item) => (
//           <div
//             key={item.id}
//             className="flex justify-between items-center bg-white p-4 rounded-lg shadow"
//           >
//             <div>
//               <p className="font-bold">{item.name}</p>
//               <p className="text-sm text-gray-500">{item.email}</p>
//             </div>

//             <button
//               onClick={() => handleDownload(item.id)}
//               className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700"
//             >
//               Download PDF
//             </button>
//           </div>
//         ))}
//       </div>
//     </div>
//   );
// };

// export default FormList;










import { useEffect, useState } from "react";
import api from "../services/api";

export default function FormList() {

  const [forms, setForms] = useState([]);

  useEffect(() => {
    api.get("").then(res => setForms(res.data));
  }, []);

  const previewPdf = id => {
    window.open(
      `http://localhost:8080/api/forms/pdf/preview/${id}`,
      "_blank"
    );
  };

  const downloadPdf = id => {
    window.location.href =
      `http://localhost:8080/api/forms/pdf/download/${id}`;
  };

  return (
    <div className="max-w-4xl mx-auto p-6">
      <h2 className="text-xl font-bold mb-4">
        Submitted Applications
      </h2>

      {forms.map(f => (
        <div
          key={f.id}
          className="flex justify-between items-center border p-4 mb-3 rounded shadow"
        >
          <div>
            <p className="font-semibold">{f.name}</p>
            <p className="text-sm text-gray-600">{f.subject}</p>
          </div>

          <div className="space-x-3">
            <button
              onClick={() => previewPdf(f.id)}
              className="px-4 py-1 bg-green-600 text-white rounded"
            >
              Preview
            </button>

            <button
              onClick={() => downloadPdf(f.id)}
              className="px-4 py-1 bg-blue-600 text-white rounded"
            >
              Download
            </button>
          </div>
        </div>
      ))}
    </div>
  );
}


