import './App.css';
import React, {useState} from 'react';

function TextBox(props) {

    const [prev, setPrev] = useState("FAKE");

    // const handleChange = (event) => {props.change(event.target.value)}
    const handleChange = (event) => {
        if (props.value !== "" && props.value!==prev) {
            props.change(props.value);
            setPrev(props.value);
            console.log("propvalue cahnged?");
            console.log(props.test);
        } else {
            props.change(event.target.value);
            props.setValue(event.target.value);
            setPrev(event.target.value);
            console.log(event.target.value);
            console.log("prop value changed^?");
        }
    }
  return (
    <div className="TextBox">
        <label> {props.label} </label>
        <input type={'text'} value = {props.value} onChange = {handleChange}/>
    </div>
  );
}

export default TextBox;
