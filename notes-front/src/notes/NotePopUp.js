import React, {Component} from "react";
import NoteService from "../services/NoteService";

import "./Popup.css"
import {FcCancel, FcCheckmark} from "react-icons/all";

class NotePopUp extends Component {
    characterLimit = 250;

    constructor(props) {
        super(props);

        this.state = {content: this.props.text}
        this.handleChange = this.handleChange.bind(this);
        this.handleUpdate = this.handleUpdate.bind(this);
    }

    handleChange(e) {
        if (this.characterLimit - e.target.value.length >= 0) {
            this.setState({
                [e.target.name]: e.target.value
            });
        }
    }

    handleUpdate(e) {
        const id = this.props.id;
        const {content} = this.state;

        e.preventDefault()
        NoteService.updateNote(id, content).then(() => {
            window.location.reload();
        })
    }

    render() {
        return (
            <div className="popup">
                <div className="editArena">
                    <form>
                        <textarea rows="10" cols="27" name="content"
                                  value={this.state.content}
                                  onChange={this.handleChange}/>

                        <div className="footer">
                            <FcCancel className="cancelButton" type="button" onClick={this.props.closePopup}/>
                            <small
                                className="characterLimit">{this.characterLimit - this.state.content.length}Remaining</small>
                            <FcCheckmark className="updateButton" type="submit" onClick={this.handleUpdate}/>
                        </div>
                    </form>
                </div>
            </div>
        );
    }
}

export default NotePopUp;