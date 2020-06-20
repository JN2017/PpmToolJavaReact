import React, { Component } from "react";
import { Route, Redirect } from "react-router-dom";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import securityReducer from "../reducers/securityReducer";

//To secure the private routes and redirect them to login page always before a vialid token
const SecuredRoute = ({ component: Component, security, ...otherProps }) => (
  <Route
    {...otherProps}
    render={(props) =>
      securityReducer.validToken === true ? (
        <Component {...props} />
      ) : (
        <Redirect to="login" />
      )
    }
  />
);

SecuredRoute.propTypes = {
  security: PropTypes.object.isRequired,
};

const mapStateToProps = (state) => ({
  security: state.security,
});

export default connect(mapStateToProps)(SecuredRoute);
