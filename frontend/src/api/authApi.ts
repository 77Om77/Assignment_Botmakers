import axiosInstance from "./axiosInstance";
import type { LoginRequest, RegisterRequest, AuthResponse } from "../types";



export const registerUser = async (data: RegisterRequest): Promise<AuthResponse> => {
  const response = await axiosInstance.post<AuthResponse>("/api/auth/register", data);
  return response.data;
};


export const loginUser = async (data: LoginRequest): Promise<AuthResponse> => {
  const response = await axiosInstance.post<AuthResponse>("/api/auth/login", data);
  return response.data;
};
