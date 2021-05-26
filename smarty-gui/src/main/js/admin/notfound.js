import React from "react";
import {Link} from 'react-router-dom';
import "bootstrap/dist/css/bootstrap.css";

class Root extends React.Component {
    // TODO: We will make a more interesting 404 page
    render() {
        return (
            <div>
                <h2> Page is not found </h2>
                <Link to="/">Root</Link><br/>
            </div>
        )
    }
}

export default Root;