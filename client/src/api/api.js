const SERVER_URL = 'http://localhost:9090'

function getServices(username) {  
  return fetch(`${SERVER_URL}/${username}/services`)
}

function addService(username, name, url) {
  return fetch(`${SERVER_URL}/${username}/services`, {
      method: "POST",
      headers: {"Content-Type": "application/json"},
      body: JSON.stringify({
        name: name,
        url: url
      })
  })
}
  
function removeService(username, id) {
  return fetch(`${SERVER_URL}/${username}/services/${id}`, {  method: "DELETE" })
}
  
export { getServices, addService, removeService }