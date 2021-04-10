import NewPost from './NewPost';

function Feed() {
        return (
            <div className="feed">
                <p className="feedTitle pageTitle">what your friends are saying</p>
                <NewPost username="glee" imgURL="https://prods3.imgix.net/images/articles/2016_08/Non-Feature-Photo-Editing-Apps-Iphone-Instagram-Snapseed-Facetune-vsco-Photoshop-Fix-Lightroom-Mobile-Photo-Justin-Schuble-_dcfoodporn.jpg"></NewPost>
                <NewPost username="ckim60" imgURL="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR_YWn5G1IGVe-9vJ8czFtwzcuf3jRhAVd2hQ&usqp=CAU"></NewPost>
            </div>
        );
}

export default Feed;
