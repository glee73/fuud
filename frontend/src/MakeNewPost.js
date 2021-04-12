import axios from "axios";
function MakeNewPost() {
    function sendRestaurantName() {
        let restaurantName = document.getElementById('restaurantName').value;
        console.log(restaurantName);
        const toSend = {
            "restaurantName": restaurantName
        }

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        };

        console.log("before");
        axios.post(
            'http://localhost:4567/addpost',
            toSend,
            config
        )
            .then(response => {
                console.log(response.data)
            })
            .catch(function (error) {
                console.log(error);
            });
        console.log("after");
    }
    return (
        <div className="makeNewPost">
            <h1 className="newPostHeader">Make a New Post!</h1>
            <div className="step step1">
                <h1 className="stepHeading">Step 1:</h1>
                <form className="formStyle restaurantNameForm">
                    <label htmlFor="restaurantName">Restaurant Name:</label>
                    <input type="text" id="restaurantName" name="restaurantName" required/>
                    <button className="submitButton searchButton" type="submit" onClick={sendRestaurantName}>Search</button>
                </form>
            </div>
            <div className="step step2">
                <h1 className="stepHeading">Step 2:</h1>
                <form className="formStyle rating">
                    <label htmlFor="rating">Rating (out of 10):</label>
                    <input type="number" id="rating" name="rating" required/>
                </form>
            </div>
            <div className="step step3">
                <h1 className="stepHeading">Step 3:</h1>
                <form className="formStyle caption">
                    <label htmlFor="caption">Caption:</label>
                    <input type="text" id="caption" name="caption" required/>
                </form>
            </div>
            <div className="step step4">
                <h1 className="stepHeading">Step 4:</h1>
                <form className="formStyle caption">
                    <label htmlFor="fileUpload">Upload Image (optional):</label>
                    <input type="text" id="fileUpload" name="fileUpload" placeholder="Please provide a public Google Drive link"/>
                </form>
            </div>
            <button className="submitButton" type="submit">Submit</button>
        </div>
    );
}

export default MakeNewPost;
