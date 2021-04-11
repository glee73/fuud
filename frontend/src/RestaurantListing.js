
function RestaurantListing(props) {
    return (
        <div className="postContainer restaurantContainer">
            <img className="restaurantImage" src={props.imgURL} />
            <div className="restaurantContent">
                <p className="restaurantTitle">{props.title}</p>
                <img className="stars" src="https://ecodri.com.au/wp-content/uploads/2019/08/5-Star-rating.png" />
                <p className="restaurantDesc">description of restaurant</p>
                <p className="hours">hours: __ - __</p>
                <p className="options">deliver/dining options</p>
                <p className="review">“Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut!”</p>
            </div>
        </div>

    )
        ;
}

export default RestaurantListing;
