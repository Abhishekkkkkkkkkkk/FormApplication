// import Form from "./components/Form";
// import FormList from "./components/FormList";

// function App() {
//   return (
//     <div className="min-h-screen bg-gray-100 p-6">
//       <Form refresh={() => window.location.reload()} />
//       <FormList />
//     </div>
//   );
// }

// export default App;



import Form from "./components/Form";
import FormList from "./components/FormList";

export default function App() {
  return (
    <>
      <Form />
      <FormList />
    </>
  );
}

