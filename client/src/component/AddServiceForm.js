import React, { useState } from 'react';
import './AddServiceForm.css';

export default function AddServiceForm(props) {
  
  const [name, setName] = useState("");
  const [url, setUrl] = useState("");
  
  function clearInputs() {
    setName("");
    setUrl("");
  }
  
  function submitForm(e) {
    e.preventDefault();
    let name =  e.target.elements[0].value;
    let url  =  e.target.elements[1].value;
    props.add(name, url);
    clearInputs();
  }
  
  return (
    <form onSubmit={submitForm}>
      <div className="form-group">
        <label htmlFor="inputName">Name</label>
        <input value={name} onChange={e=>setName(e.target.value)} type="text" className="form-control" id="name" name="name" placeholder="Enter name" required/>
      </div>
      <div className="form-group">
        <label htmlFor="inputURL">URL</label>
        <input value={url} onChange={e=>setUrl(e.target.value)} type="text" className="form-control" id="url" name="url" placeholder="Enter URL" required/>
      </div>
      <div className="text-center">
      <button type="submit" className="btn btn-primary">Add service</button>
      </div>
    </form>
  );
}
