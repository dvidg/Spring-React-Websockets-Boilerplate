import { Button, FormGroup, FormControl} from "react-bootstrap";
import React, {Component } from 'react';
import Stomp from 'stompjs';

export default class Login extends Component {
  constructor() {
  super();
  this.state = {
  	sampleRes: false,
    message: "",
    sampleMessage: "",
    messageResponse: "",
  };


  this.connection = 'ws://localhost:8080';
  this.stompClient = Stomp.client(this.connection);

  this.stompClient.connect({"user" : this.state.user}, frame => {

    setInterval(() => this.stompClient.send("/ping", {}, JSON.stringify({ event: "ping" })), 10000);

    this.stompClient.subscribe("/topic/returnMessage", response => {
                this.setState( { messageResponse: JSON.stringify(response.body) } );
            });
    });
  }

  validateForm() {
    return this.state.message.length > 0;
  }

  handleChange = event => {
    this.setState({
      [event.target.id]: event.target.value
    });
  }

  handleSubmit = event => {
    var message = this.state.message
    event.preventDefault();
    this.stompClient.send("/sampleMessage", {}, JSON.stringify({message}));
  }


  render() { 
      return (
        <div>
          <h1> Simple SpringBoot-React Boilerplate</h1>
          <div>
            <form onSubmit= {this.handleSubmit}>
              <FormGroup controlId="message" bsSize="large">
                <FormControl
                  autoFocus
                  type="message"
                  value={this.state.message}
                  onChange={this.handleChange}
                />
              <Button
                block
                bsSize="large"
                disabled={!this.validateForm()}
                type="submit">
                Play!
              </Button>
             </FormGroup>
            </form>
          </div>
          {this.state.messageResponse}
        </div>
      );
  }
}
