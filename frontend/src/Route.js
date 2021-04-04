import './App.css';
import TextBox from "./TextBox.js";
import React, {useState, useEffect} from 'react';

import { AwesomeButton } from "react-awesome-button";
import "react-awesome-button/dist/styles.css";
import axios from 'axios';


function Route(props) {

    // const [routeStartLat, setRouteStartLat] = useState(0);
    // const [routeStartLon, setRouteStartLon] = useState(0);
    // const [routeEndLat, setRouteEndLat] = useState(0);
    // const [routeEndLon, setRouteEndLon] = useState(0);
    // const [route, setRoute] = useState(0);
    const [printRoute, setPrintRoute] = useState(null);


/**
   * Makes an axios request.
   */
  const requestRoute = () => {
      const toSend = {

        srclat: props.startLat,
        srclong: props.startLon,
        destlat: props.endLat,
        destlong: props.endLon
      };

      let config = {
          headers: {
            "Content-Type": "application/json",
            'Access-Control-Allow-Origin': '*',
            }
          }

        axios.post(
        "http://localhost:4567/route",
        toSend,
        config
      )
        .then(response => {
            console.log('button pressed');
          console.log(response.data);
          setPrintRoute(response.data["route"]);
          props.setRoute(response.data["routeCoor"]);
        })

        .catch(function (error) {
          console.log(error);

        });
      }

      // useEffect(() => {
      //
      // }, [props.startLat, props.startLon, props.endLat, props.endLon]);

  return (
    <div className="Route">
        <TextBox label={"Start Latitude"} change={props.setStartLat} />
        <TextBox label={"Start Longitude"} change={props.setStartLon} />
        <TextBox label={"End Latitude"} change={props.setEndLat} />
        <TextBox label={"End Longitude"} change={props.setEndLon} />
        <AwesomeButton type="primary" onPress={requestRoute} >Button</AwesomeButton>
        <div>
            <p> Answer: </p>
            {printRoute}
        </div>
    </div>
  );
}

export default Route;
