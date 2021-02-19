import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import AddServiceForm from './AddServiceForm';
import './Services.css'
import { getServices, addService, removeService } from '../api/api.js'

export default function Services(props) {
  let { username } = useParams();
  const [services, setServices] = useState([]);

  useEffect(() => {
    update()
    // eslint-disable-next-line
  }, []);
  
  function update() {
    getServices(username)
    .then(response => response.json())
    .then(data => setServices(data))
  }
  
  function addHandler(name, url) {
    addService(username, name, url)
    .then(update)
  }
  
  function removeHandler(id) {
    removeService(username, id)
    .then(update)
  }

  return (
    <div>
      <table className="table">
        <thead className="thead-dark">
          <tr>
            <th>Name</th>
            <th>URL</th>
            <th>Created</th>
            <th>Last update</th>
            <th>Status</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {
            services.map((service, i) => 
              <Service remove={removeHandler} data={service} key={i} />)}
        </tbody>
      </table>
      {<AddServiceForm add={addHandler}/>}
    </div>
  )
}

function Service(props) {
  return (
    <tr>
      <td>{props.data.name}</td>
      <td>{props.data.url}</td>
      <td>{props.data.created}</td>
      <td>{props.data.updated}</td>
      <td><span className={`status ${props.data.status ? 'green blink' : 'red'}`}></span></td>
      <td><button onClick={e => props.remove(props.data.id)}>Remove</button></td>
    </tr>
  );
}
