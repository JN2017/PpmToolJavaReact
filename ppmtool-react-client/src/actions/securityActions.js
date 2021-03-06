import axios from "axios";
import { GET_ERRORS, SET_CURRENT_USER } from "./types";
import { setJWTToken } from "../securityUtils/setJWTToken";
import jwt_decode from "jwt-decode";

export const createNewUser = (newUser, history) => async (dispatch) => {
  try {
    await axios.post("/api/users/register", newUser);
    history.push("/login");
    dispatch({
      type: GET_ERRORS,
      payload: {},
    });
  } catch (err) {
    dispatch({
      type: GET_ERRORS,
      payload: err.response.data,
    });
  }
};

export const login = (LoginRequest) => async (dispatch) => {
  try {
    //post login => request login
    const res = await axios.post("/api/users/login", LoginRequest);
    // extract the token from res.data
    const { token } = res.data;
    //store the token on the local storage
    localStorage.setItem("jwtToken", token);
    //set our token in the header ***
    setJWTToken(token);
    //decode the token on react
    const decoded = jwt_decode(token);
    //dispatch to our Security reducers
    dispatch({
      type: SET_CURRENT_USER,
      payload: decoded,
    });
  } catch (err) {
    dispatch({
      type: GET_ERRORS,
      payload: err.response.data,
    });
  }
};

export const logout = () => (dispatch) => {
  localStorage.removeItem("jwtToken");
  setJWTToken(false);
  dispatch({
    type: SET_CURRENT_USER,
    payload: {},
  });
};
