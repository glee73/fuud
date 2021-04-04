import './App.css';
import React, {useState, useRef, useEffect} from 'react';
import axios from 'axios';
import { AwesomeButton } from "react-awesome-button";
import "react-awesome-button/dist/styles.css";
import TextBox from "./TextBox.js";

function Canvas() {

    const width = 500;
    const height = 500;

    const [startlat, setStartLat] = useState(41.8288782431490);
    const [startlon, setStartLon] = useState(-71.40530626699541);
    const [endlat, setEndLat] = useState(41.82418530886059);
    const [endlon, setEndLon] = useState(-71.39894406784666);

    const [dragging, setDragging] = useState(false);
    const [x, setX] = useState(0);
    const [y, setY] = useState(0);

    const [scrolling, setScrolling] = useState(true);

    const canvasRef = useRef(null)
    
    //route stuff
    const [routeStartLat, setRouteStartLat] = useState(0);
    const [routeStartLon, setRouteStartLon] = useState(0);
    const [routeEndLat, setRouteEndLat] = useState(0);
    const [routeEndLon, setRouteEndLon] = useState(0);
    // const [route, setRoute] = useState([]);

    const [printRoute, setPrintRoute] = useState(null);

    const tileLength = .005; // each tile is a .005 x .005 box

    // cache dictionary that maps a const array of coordinates to a list of ways (way = double[])
    const [cache2, setCache2] = useState({});

    //test for updating textbox values
    const [t1, setT1] = useState("");
    const [t2, setT2] = useState("");
    const [t3, setT3] = useState("");
    const [t4, setT4] = useState("");

    /**
     * Handles logic of getting ways in a bounding box.
     * Inputted parameters are bounding box coordinates.
     * Handles tiling by getting/storing information from cache or calling
     * reqWays (axios post request to get ways) accordingly.
     */
    const requestWays = (startlatInput, startlonInput, endlatInput, endlonInput) => {

        /*
        we want to get the topmost point of the tile for the TOPLEFT corner of the box
         */
        let tilesBoxStartLat;
        if ((startlatInput % tileLength) > 0) {
            tilesBoxStartLat = startlatInput + (tileLength - (startlatInput % tileLength));
        } else {
            tilesBoxStartLat = startlatInput;
        }

        /*
        we want to get the leftmost point of the tile for the TOPLEFT corner of the box
         */
        let tilesBoxStartLon = startlonInput - (startlonInput % tileLength) - tileLength;

        /*
        we want to get the topmost point of the tile for the BOTTOMRIGHT corner of the box
         */
        let tilesBoxEndLat;
        if ((endlatInput % tileLength) > 0) {
            tilesBoxEndLat = endlatInput + (tileLength - (endlatInput % tileLength));
        } else {
            tilesBoxEndLat = endlatInput;
        }

        /*
        we want to get the leftmost point of the tile for the BOTTOMRIGHT corner of the box
         */
        let tilesBoxEndLon = endlonInput - (endlonInput % tileLength);

        // redraw map square
        const canvas = canvasRef.current
        const ctx = canvas.getContext('2d')

        ctx.clearRect(0, 0, width, height);
        ctx.fillStyle = "#d4ecff";
        ctx.fillRect(0, 0, width, height);
        //drawing border
        ctx.strokeStyle = '#000000'
        ctx.lineWidth = 1;
        ctx.beginPath();
        ctx.moveTo(0,0);
        ctx.lineTo(width, 0);
        ctx.stroke();
        ctx.lineTo(width, height);
        ctx.stroke();
        ctx.lineTo(0, height);
        ctx.stroke();
        ctx.lineTo(0,0);
        ctx.stroke();
        ctx.closePath();

        tilesBoxStartLat = parseFloat(tilesBoxStartLat.toPrecision(5))
        tilesBoxStartLon = parseFloat(tilesBoxStartLon.toPrecision(5))
        tilesBoxEndLat = parseFloat(tilesBoxEndLat.toPrecision(5))
        tilesBoxEndLon = parseFloat(tilesBoxEndLon.toPrecision(5))

        // we need to now loop through each of the tiles and check if already have the ways
        for (let i = tilesBoxStartLat; parseFloat(i.toPrecision(5)) >= tilesBoxEndLat; i-=tileLength) {
            for (let j = tilesBoxStartLon; parseFloat(j.toPrecision(5)) <= tilesBoxEndLon; j+=tileLength) {
                const coordsKey = [parseFloat(i.toPrecision(5)), parseFloat(j.toPrecision(5))];
                let coordKeyAsString = String(coordsKey[0]) + String(coordsKey[1]);
                if (coordKeyAsString in cache2) {
                    for (const [key, value] of Object.entries(cache2)) {
                        if (key === coordKeyAsString){
                            // concat onto totalWays
                            drawWay(ctx, value, startlatInput, startlonInput, endlatInput, endlonInput);
                        }
                    }
                } else {
                    reqWays(coordKeyAsString, parseFloat(i.toPrecision(5)), parseFloat(j.toPrecision(5)),
                        parseFloat((i - tileLength).toPrecision(5)),
                        parseFloat((j + tileLength).toPrecision(5)),
                        startlatInput, startlonInput, endlatInput, endlonInput);
                    //reqWays now handles querying of those ways, adding them to the cache
                    //and drawing that tile
                }
            }
        }
    }

    /**
     * makes a post request to retrieve ways within the given bounding box.
     * Called in the requestWays method, if ways are not in cache.
     */
    const reqWays = (coordKey, startlatInput, startlonInput, endlatInput, endlonInput, startlatMap, startlonMap, endlatMap, endlonMap) => {
            const toSend = {
                srclat: startlatInput,
                srclong: startlonInput,
                destlat: endlatInput,
                destlong: endlonInput
            };

            let config = {
                headers: {
                    "Content-Type": "application/json",
                    'Access-Control-Allow-Origin': '*',
                }
            }

            axios.post(
                "http://localhost:4567/ways",
                toSend,
                config
            )
                .then(response => {
                    console.log(response.data);
                    let waysReturn = response.data["ways"];
                    //adding to the cache!
                    cache2[coordKey] = response.data["ways"];
                    setCache2(cache2);
                    const canvas = canvasRef.current
                    const context = canvas.getContext('2d')
                    drawWay(context, waysReturn, startlatMap, startlonMap, endlatMap, endlonMap)
                })

                .catch(function (error) {
                    console.log(error);
                });
    }

    /**
     * Makes an axios request to get nearest traversable node
     * to where the user clicked on the canvas.
     */
    const requestNearest = (clickY, clickX) => {

        const toSend = {
            lat: ((clickY/height)*(endlat - startlat) + startlat),
            lon: ((clickX/width)*(endlon - startlon) + startlon),
        };
        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }
        axios.post(
            "http://localhost:4567/nearest",
            toSend,
            config
        )
            .then(response => {
                console.log(response.data);
                let lat = response.data["nearest"][0];
                let lon = response.data["nearest"][1];

                //this would be first
                if (t1 === "" || t2 === ""){
                    setT1(lat);
                    setT2(lon);
                    const canvas = canvasRef.current;
                    const context = canvas.getContext('2d');
                    context.clearRect(0, 0, width, height);
                    context.fillStyle = "#d4ecff";
                    context.fillRect(0, 0, width, height);
                    requestWays(startlat, startlon, endlat, endlon);

                    setRouteStartLat(lat);
                    setRouteStartLon(lon);
                    context.strokeStyle = '#000000'
                    context.lineWidth = 1;
                    context.beginPath();
                    let cX = width * [(lon - startlon) / (endlon - startlon)];
                    let cY = height * [(lat - startlat) / (endlat - startlat)];
                    context.arc(cX, cY, 5, 0, 2 * Math.PI);
                    context.stroke();
                } else {
                    setT3(lat);
                    setT4(lon);
                    setRouteEndLat(lat);
                    setRouteEndLon(lon);
                    requestRoute(routeStartLat, routeStartLon, lat, lon);

                }
            })
            .catch(function (error) {
                console.log(error);
            });
    }

    /**
     * Makes an axios request to get route between traversable nodes
     * or street names.
     */
    const requestRoute = (rstartlat, rstartlon, rendlat, rendlon) => {
        const toSend = {
            srclat: rstartlat,
            srclong: rstartlon,
            destlat: rendlat,
            destlong: rendlon
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
                console.log(response.data);
                setPrintRoute(response.data["route"]);
                 // setRoute(response.data["routeCoor"]);

                if(response.data["routeCoor"].length === 0 || response.data["routeCoor"].length===1){
                    setRouteStartLat(0);
                    setRouteEndLat(0);
                    setRouteStartLon(0);
                    setRouteEndLon(0);
                    setT1("");
                    setT2("");
                    setT3("");
                    setT4("");
                    requestWays(startlat, startlon, endlat, endlon);
                }

                if (response.data["routeCoor"].length !== 0 && response.data["routeCoor"].length!==1){
                    drawRoute(response.data["routeCoor"]);
                }

            })

            .catch(function (error) {
                console.log(error);

            });
    }

    /**
     * Method that handles drawing the ways on the canvas.
     * @param ctx of canvas
     * @param ways list of way information
     * @param startlatInput coordinate of canvas bounding box
     * @param startlonInput coordinate of canvas bounding box
     * @param endlatInput coordinate of canvas bounding box
     * @param endlonInput coordinate of canvas bounding box
     */
    const drawWay = (ctx, ways, startlatInput, startlonInput, endlatInput, endlonInput) => {
        if (ways === undefined) {
            return
        }

        ctx.lineWidth = 1;
        for (let i = 0; i < ways.length; i++) {
            let way = ways[i];
            if ( ((way[0] <= startlatInput) && (way[0] >= endlatInput) && (way[1] >= startlonInput) && (way[1] <= endlonInput))
                || ((way[2] <= startlatInput) && (way[2] >= endlatInput) && (way[3] >= startlonInput) && (way[3] <= endlonInput)) ) {
                // in bounds case!
                if (way[4] === 1) {
                    ctx.strokeStyle = '#00ff00'
                } else {
                    ctx.strokeStyle = '#FF0000'
                }
                ctx.beginPath()
                let startX = width * [(way[1] - startlonInput) / (endlonInput - startlonInput)]
                let startY = height * [(way[0] - startlatInput) / (endlatInput - startlatInput)]
                let endX = width * [(way[3] - startlonInput) / (endlonInput - startlonInput)]
                let endY = height * [(way[2] - startlatInput) / (endlatInput - startlatInput)]
                ctx.moveTo(startX,startY);
                ctx.lineTo(endX,endY);

                // scale just end x and end y
                // find dif btwn top and bottom of box and multiply that dif by scale factor
                ctx.stroke();
            } else {
            }
        }
    }

    /**
     * Method that is called on mouseDown.
     * stores information about where user clicked.
     * @param event mouseDown
     */
    const clicked = (event) => {
        setDragging(true);
        setX(event.pageX);
        setY(event.pageY);
    }

    /**
     * Method that is called on mouseUp.
     * Handles clicking and panning logic accordingly
     * @param event mouseUp
     */
    const unclicked = (event) => {
        if (dragging === true) {
            //clicking as opposed to panning
            if ((event.pageX - x) ===0 && (event.pageY - y)===0){

                const canvas = canvasRef.current
                const context = canvas.getContext('2d')

                let clickX =(event.pageX - canvas.offsetLeft);
                let clickY = (event.pageY - canvas.offsetTop);

                requestNearest(clickY, clickX);
                setDragging(false);
            } else{
                let differenceX = ((event.pageX - x) / width) * .01;
                let differenceY = ((y - event.pageY) / height) * .01;

                requestWays(startlat - differenceY, startlon - differenceX, endlat - differenceY, endlon - differenceX);
                setStartLat(startlat - differenceY)
                setEndLat(endlat - differenceY)
                setStartLon(startlon - differenceX)
                setEndLon(endlon - differenceX)
                setDragging(false);
            }
        }
    }

    /**
     * Helper method for zooming in.
     * @param event onWheel
     */
    const zoomInOut = (event) => {
            let distX = endlon - startlon;
            let distY = endlat - startlat;
            //zooming in
            if (event.deltaY < 0) {
                let scale = 1 - 0.1 * event.deltaY;
                let endDistX = distX * scale;
                let endDistY = distY * scale;
                requestWays(startlat + (distY - endDistY)/2, startlon + (distX - endDistX)/2, endlat - (distY - endDistY)/2, endlon - (distX - endDistX)/2);
                setStartLon(startlon + (distX - endDistX) / 2);
                setEndLon(endlon - (distX - endDistX) / 2);
                setStartLat(startlat + (distY - endDistY) / 2);
                setEndLat(endlat - (distY - endDistY) / 2);
            } else {
                let scale = 1 + 0.1 * event.deltaY;
                let endDistX = distX * scale;
                let endDistY = distY * scale;
                requestWays(startlat - (distY - endDistY)/2, startlon - (distX - endDistX)/2, endlat + (distY - endDistY)/2, endlon + (distX - endDistX)/2);
                setStartLon(startlon - (distX - endDistX) / 2);
                setEndLon(endlon + (distX - endDistX) / 2);
                setStartLat(startlat - (distY - endDistY) / 2);
                setEndLat(endlat + (distY - endDistY) / 2);
            }
    }

    /**
     * Method that is called via onWheel.
     * Handles zooming in and out of map.
     * Sets timers and calls zoomInOut helper method.
     * @param event onWheel.
     */
    const startZoom = (event) => {
        if (scrolling ===true){
            setScrolling(false);
            setTimeout( () => {
                zoomInOut(event);
            }, 1000);
        } setTimeout( () => {
            setScrolling(true);
            }, 2000);
    }

    /**
     * Method that is in charge of drawing route on canvas.
     * @param route to be drawn between two traversable nodes.
     */
    const drawRoute = (route) => {
        //if route is off screen will probably have to query for new start/end lat/lon and ways
        //do this just by calling requestWays
        const canvas = canvasRef.current;
        const context = canvas.getContext('2d');
        context.clearRect(0, 0, width, height);
        context.fillStyle = "#d4ecff";
        context.fillRect(0, 0, width, height);

        //coordinates for new bounding box if route is outside current scope
        let newStartLat = startlat;
        let newEndLat = endlat;
        let newStartLon = startlon;
        let newEndLon = endlon;
        let outside = false; //variable to determine if scope of box needs to be changed
        //checking if any routes are outside of scope of current box
        for (let i = 0; i< route.length; i++) {
            let routeLat = route[i][1];
            let routeLon = route[i][0];
            if (routeLat >= newStartLat || routeLat <= newEndLat || routeLon <= newStartLon || routeLon >= newEndLon){
                outside = true;
            }
        }
        //route is outside current scope
        if (outside === true){
            newStartLat = route[0][1];
            newEndLat = route[0][1];
            newStartLon = route[0][0];
            newEndLon = route[0][0];
            for (let i = 1; i< route.length; i++) {
                let routeLat = route[i][1];
                let routeLon = route[i][0];
                if (routeLat >= newStartLat || routeLat <= newEndLat || routeLon <= newStartLon || routeLon >= newEndLon){
                    if (routeLat >= newStartLat) {
                        newStartLat = routeLat;
                    }
                    if (routeLat <= newEndLat) {
                        newEndLat = routeLat;
                    }
                    if (routeLon <= newStartLon) {
                        newStartLon = routeLon;
                    }
                    if (routeLon >= newEndLon) {
                        newEndLon = routeLon;
                    }
                }
            }
            let buffer = 0.0005;
            newStartLat = newStartLat + buffer;
            newEndLat = newEndLat - buffer;
            newStartLon = newStartLon - buffer;
            newEndLon = newEndLon + buffer;
            //making sure new scope maintains square dimensions
            let latDist = newStartLat - newEndLat;
            let lonDist = newEndLon - newStartLon;
            if (latDist > lonDist){
                newStartLon = newStartLon - ((latDist - lonDist)/2);
                newEndLon = newEndLon + ((latDist - lonDist)/2);
            } else if (lonDist > latDist){
                newStartLat = newStartLat + ((lonDist- latDist)/2);
                newEndLat = newEndLat - ((lonDist- latDist)/2);
            }
        }
        requestWays(newStartLat, newStartLon, newEndLat, newEndLon);
        setStartLat(newStartLat);
        setStartLon(newStartLon);
        setEndLat(newEndLat);
        setEndLon(newEndLon);

        context.strokeStyle = '#000000'
        context.beginPath();
        let cX1 = width * [(route[0][0] - newStartLon) / (newEndLon - newStartLon)];
        let cY1 = height * [(route[0][1] - newStartLat) / (newEndLat - newStartLat)];
        context.arc(cX1, cY1, 5, 0, 2 * Math.PI);
        context.stroke();
        let cX2 = width * [(route[route.length-1][0] - newStartLon) / (newEndLon - newStartLon)];
        let cY2 = height * [(route[route.length-1][1] - newStartLat) / (newEndLat - newStartLat)];
        context.moveTo(cX2, cY2);
        context.arc(cX2, cY2, 5, 0, 2 * Math.PI);
        context.stroke();

        context.lineWidth = 3;

        for (let i = 1; i< route.length; i++){
            let rs = route[i-1];
            let re = route[i];
            context.beginPath()

            let startX = width * [(rs[0] - newStartLon) / (newEndLon - newStartLon)];
            let startY = height * [(rs[1] - newStartLat) / (newEndLat - newStartLat)];
            context.moveTo(startX,startY);

            let endX = width * [(re[0] - newStartLon) / (newEndLon - newStartLon)]
            let endY = height * [(re[1] - newStartLat) / (newEndLat - newStartLat)]
            context.lineTo(endX,endY);
            context.stroke();
        }
        setRouteStartLat(0);
        setRouteEndLat(0);
        setRouteStartLon(0);
        setRouteEndLon(0);
        setT1("");
        setT2("");
        setT3("");
        setT4("");
        //resetting route parameters

    }

    /**
     * Called on application start, to initialize canvas with map.
     */
    useEffect(() => {
        requestWays(startlat, startlon, endlat, endlon);
    }, []);

      return (
        <div>
            <p> Enter Starting/Ending Longitudes/Latitudes or Street Names in quotes:
            </p>
            <div>
                <TextBox id= "textbox1" value={t1} setValue = {setT1} test={routeStartLat} label={"Start Latitude/ Street 1:"} change={setRouteStartLat} />
                <TextBox id= "textbox2" value={t2} setValue = {setT2} label={"Start Longitude/ Cross-street 1:"} change={setRouteStartLon} />
                <TextBox id= "textbox3" value={t3} setValue = {setT3} label={"End Latitude/ Street 2:"} change={setRouteEndLat} />
                <TextBox id= "textbox4" value={t4} setValue = {setT4} label={"End Longitude/ Cross-street 2:"} change={setRouteEndLon} />
                <AwesomeButton type="primary" onPress={() => requestRoute(routeStartLat, routeStartLon, routeEndLat, routeEndLon)} >Find My Route!</AwesomeButton>
            </div>
            <canvas id= "myCanvas" onMouseUp={unclicked} onMouseDown={clicked} onWheel={startZoom} ref={canvasRef} width={width} height={height}/>
            <div>
                <p> Answer: </p>
                {printRoute}
            </div>
        </div>
    );
}

export default Canvas;
