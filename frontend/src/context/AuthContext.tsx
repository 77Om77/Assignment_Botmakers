import React, { createContext, useContext, useState } from "react";
import type { User } from "../types";


interface AuthContextType {
  user: User | null;        
  token: string | null;     
  login: (token: string, user: User) => void;   
  logout: () => void;       
  isLoggedIn: boolean;      
}


const AuthContext = createContext<AuthContextType | null>(null);


export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {

  const [token, setToken] = useState<string | null>(() => {
    return localStorage.getItem("token");
  });

  const [user, setUser] = useState<User | null>(() => {
    const saved = localStorage.getItem("user");
    return saved ? JSON.parse(saved) : null;  
  });

  
  const login = (newToken: string, newUser: User) => {
    setToken(newToken);
    setUser(newUser);
    
    localStorage.setItem("token", newToken);
    localStorage.setItem("user", JSON.stringify(newUser));
  };

  
  const logout = () => {
    setToken(null);
    setUser(null);
    
    localStorage.removeItem("token");
    localStorage.removeItem("user");
  };

  const value: AuthContextType = {
    user,
    token,
    login,
    logout,
    isLoggedIn: !!token,  
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};


export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (!context) {
    
    throw new Error("useAuth must be used inside AuthProvider");
  }
  return context;
};
