import axiosInstance from "../api/axiosInstance";
import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";

const DashboardPage = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [apiResult, setApiResult] = useState<string | null>(null);
  const [apiError, setApiError] = useState<string | null>(null);

  useEffect(() => {
    if (!user) {
      navigate("/login");
    }
  }, [user, navigate]);

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  const testUserEndpoint = async () => {
    setApiError(null);
    try {
      const res = await axiosInstance.get("/api/user");
      setApiResult(JSON.stringify(res.data, null, 2));
    } catch {
      setApiError("403 Forbidden - You don't have access to this endpoint.");
    }
  };

  const testAdminEndpoint = async () => {
    setApiError(null);
    try {
      const res = await axiosInstance.get("/api/admin");
      setApiResult(JSON.stringify(res.data, null, 2));
    } catch {
      setApiError("403 Forbidden - Admin only. USER role cannot access this.");
    }
  };

  const testPublicEndpoint = async () => {
    setApiError(null);
    try {
      const res = await axiosInstance.get("/api/public");
      setApiResult(JSON.stringify(res.data, null, 2));
    } catch {
      setApiError("Something went wrong.");
    }
  };

  const isAdmin = user?.role === "ADMIN";

  return (
    <div className="min-h-screen bg-gray-900 flex flex-col">
     
      <nav className="bg-gray-800 border-b border-gray-700 px-6 py-3 flex items-center justify-between sticky top-0 z-10">
        <div className="flex items-center gap-2">
          <div className="w-8 h-8 bg-blue-600 rounded-md flex items-center justify-center">
            🔐
          </div>
          <span className="font-bold text-white">AuthRBAC</span>
        </div>
        <div className="flex items-center gap-4">
          <span
            className={`px-3 py-1 text-xs font-semibold rounded-full ${
              isAdmin
                ? "bg-purple-900 text-purple-200 border border-purple-700"
                : "bg-blue-900 text-blue-200 border border-blue-700"
            }`}
          >
            {isAdmin ? "Admin" : "User"}
          </span>
          <span className="text-gray-400 text-sm">{user?.name}</span>
          <button
            onClick={handleLogout}
            className="px-3 py-1 text-sm bg-red-900/50 text-red-200 border border-red-700 rounded hover:bg-red-800/50 transition-colors"
          >
            Sign out
          </button>
        </div>
      </nav>

      
      <main className="flex-1 max-w-6xl w-full mx-auto px-4 py-8">
        
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-white mb-1">
            Hello, {user?.name?.split(" ")[0]} 
          </h1>
          <p className="text-gray-400">
            Welcome to your dashboard. Your role is <strong className="text-white">{user?.role}</strong>.
          </p>
        </div>

        
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-8">
          <div className="bg-gray-800 border border-gray-700 rounded-lg p-5">
           
            <h3 className="text-xs font-semibold text-gray-400 uppercase tracking-wider mb-1">Name</h3>
            <p className="text-white font-medium">{user?.name}</p>
          </div>
          <div className="bg-gray-800 border border-gray-700 rounded-lg p-5">
           
            <h3 className="text-xs font-semibold text-gray-400 uppercase tracking-wider mb-1">Email</h3>
            <p className="text-white font-medium break-all">{user?.email}</p>
          </div>
          <div className="bg-gray-800 border border-gray-700 rounded-lg p-5">
           
            <h3 className="text-xs font-semibold text-gray-400 uppercase tracking-wider mb-1">Role</h3>
            <p className="text-white font-medium">{user?.role}</p>
          </div>
        </div>

        
        {isAdmin && (
          <div className="bg-gray-800 border border-purple-800 rounded-lg p-6 mb-6">
            <h2 className="text-lg font-semibold text-purple-300 mb-2">Admin Panel</h2>
            <p className="text-gray-400 text-sm">
              You have administrator privileges. You can access all user data, manage roles, and view
              system statistics. The <code className="text-purple-300">/api/admin</code> endpoint is
              exclusively available to you.
            </p>
          </div>
        )}

        
        <div className="bg-gray-800 border border-gray-700 rounded-lg p-6 mb-6">
          <div className="flex items-center gap-2 mb-3">
            <h2 className="text-lg font-semibold text-white">Live API Tester</h2>
            <span className="text-xs bg-blue-900 text-blue-200 px-2 py-0.5 rounded-full">Interactive</span>
          </div>
          <p className="text-gray-400 text-sm mb-4">
            Click the buttons below to call real backend endpoints with your JWT. Watch the RBAC rules in
            action.
          </p>
          <div className="flex flex-wrap gap-3 mb-4">
            <button
              onClick={testPublicEndpoint}
              className="px-4 py-2 bg-green-900/30 text-green-300 border border-green-800 rounded-md text-sm hover:bg-green-900/50 transition-colors"
            >
              GET /api/public
            </button>
            <button
              onClick={testUserEndpoint}
              className="px-4 py-2 bg-blue-900/30 text-blue-300 border border-blue-800 rounded-md text-sm hover:bg-blue-900/50 transition-colors"
            >
              GET /api/user
            </button>
            <button
              onClick={testAdminEndpoint}
              className="px-4 py-2 bg-purple-900/30 text-purple-300 border border-purple-800 rounded-md text-sm hover:bg-purple-900/50 transition-colors"
            >
              GET /api/admin
            </button>
          </div>

          {apiResult && (
            <pre className="bg-gray-950 border border-gray-700 rounded p-4 text-sm text-green-300 font-mono overflow-auto">
              {apiResult}
            </pre>
          )}
          {apiError && (
            <div className="bg-red-900/50 border border-red-700 text-red-200 px-4 py-3 rounded text-sm">
              {apiError}
            </div>
          )}
        </div>
      </main>
    </div>
  );
};

export default DashboardPage;