import React, {Component} from "react";
import NoteService from "../services/NoteService"
import {AiOutlineEdit, MdDeleteForever} from "react-icons/all";
import NotePopUp from "./NotePopUp";

import "./Note.css"

export default class Note extends Component {
    characterLimit = 250;

    constructor(props) {
        super(props);

        this.state = {content: "", responseData: [], showPopup: false, editedNoteText: "", editedNoteId: ""};

        this.handleChange = this.handleChange.bind(this);
        this.handleCreate = this.handleCreate.bind(this);
    }

    componentDidMount() {
        NoteService.getNotes().then((response) => {
            this.setState({
                responseData: response
            })
        })
    }

    togglePopup(id, text) {
        this.setState({
            editedNoteId: id,
            editedNoteText: text,
            showPopup: !this.state.showPopup
        });
    }

    renderNotes() {
        function handleDeleteNote(id) {
            if (window.confirm('Are you sure you want to delete this note?')) {
                NoteService.deleteNote(id).then(() => {
                    window.location.reload();
                });
            }
        }


        return this.state.responseData.map((notes) => (
            <div className="notesList">
                <ul key={notes.id}>
                    {notes.text}
                    <div className="icons">
                        <AiOutlineEdit
                            onClick={this.togglePopup.bind(this, notes.id, notes.text)}
                            className="editIcon"
                        />

                        <MdDeleteForever
                            onClick={() => handleDeleteNote(notes.id)}
                            className="deleteIcon"
                        />
                    </div>
                </ul>
            </div>
        ));
    };

    handleChange(e) {
        if (this.characterLimit - e.target.value.length >= 0) {
            this.setState({
                [e.target.name]: e.target.value
            });
        }
    }

    handleCreate(e) {
        const {content} = this.state;
        e.preventDefault()
        NoteService.saveNote(content).then(() => {
            window.location.reload();
        })
    }

    render() {
        return (
            <>
                <div className="notes">
                    <div className="addNote">
                        <form onSubmit={this.handleCreate}>
                            <textarea rows="10" cols="26" name="content"
                                      placeholder="Type to add a note ..."
                                      value={this.state.content}
                                      onChange={this.handleChange}/>
                            <div className="noteFooter">
                                <small>{this.characterLimit - this.state.content.length} Remaining</small>
                                <button className="saveButton" type="submit"> save</button>
                            </div>
                        </form>

                    </div>
                    <ul>{this.renderNotes()}</ul>
                </div>
                {this.state.showPopup ?
                    <NotePopUp
                        id={this.state.editedNoteId}
                        text={this.state.editedNoteText}
                        closePopup={this.togglePopup.bind(this)}
                    />
                    : null
                }
            </>
        );
    }
}