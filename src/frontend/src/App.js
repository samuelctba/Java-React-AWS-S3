import React, { useEffect, useState, useCallback } from 'react';
import { useDropzone } from 'react-dropzone'
import axios from 'axios';


const UserProfiles = () => {
  const [userProfiles, setUserProfiles] = useState([]);

  const fetchUserProfiles = () => {
    axios.get("http://localhost:8080/api/v1/user-profile/")
      .then(res => {
        setUserProfiles(res.data);
        console.log(res);
        // console.log(userProfiles);
      })
      .catch((err) => { console.log(err); })
  }

  useEffect(() => {
    fetchUserProfiles();
  }, [])

  const onFailedBackend = {
    color: "red",
    backgroundColor: "DodgerBlue",
    padding: "10px",
    fontFamily: "Arial"
  };

  return (
    userProfiles.length !== 0 ?
      userProfiles.map((userProfile, index) => {
        return (
          <div key={index}>
            {userProfile.userProfileImgLink ? <img src={`http://localhost:8080/api/v1/user-profile/${userProfile.userProfileID}/image/download`} /> : <p>No Profile Picture Available</p>}
            <h1>{userProfile.userName}</h1>
            <p>{userProfile.userProfileID}</p>
            <p>{userProfile.userProfileImgLink}</p>
            <MyDropzone {...userProfile} />
          </div>
        )
      })
      :
      <div>
        <h1 style={onFailedBackend}>Backend is Down!</h1>
      </div>
  )
};

function MyDropzone({ userProfileID }) {
  const onDrop = useCallback(acceptedFiles => {
    // Do something with the files
    const file = acceptedFiles[0];
    console.log(file);
    const formData = new FormData();
    formData.append("file", file);

    axios.post(`http://localhost:8080/api/v1/user-profile/${userProfileID}/image/upload`,
      formData,
      {
        headers: {
          "Content-Type": "multipart/form-data"
        }
      }).then((res) => {
        console.log("File Uploaded Successfully", res);
      }).catch((err) => { console.log(err) });

  }, [userProfileID]);

  const { getRootProps, getInputProps, isDragActive } = useDropzone({ onDrop });

  return (
    <div {...getRootProps()}>
      <input {...getInputProps()} />
      {
        isDragActive ?
          <p>Drop the files here ...</p> :
          <p>Drag 'n' drop some files here, or click to select files</p>
      }
    </div>
  )
}


function App() {
  return (
    <div className="App">
      <UserProfiles />
    </div>
  );
}




export default App;
