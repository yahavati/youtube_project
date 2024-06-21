import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Login.css';

const users = {
    'user1': 'password123',
    'user2': 'password456'
};

function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();
        if (!username || !password) {
            setError('All fields are required.');
            return;
        }
        if (!users[username]) {
            setError('Username does not exist.');
            return;
        }
        if (users[username] !== password) {
            setError('Password does not match.');
            return;
        }
        navigate('/home');
    };

    const handleSignUp = () => {
        navigate('/signup');
    };

    return (
        <div className="login-container">
            <div className="login-box">
                <img src="/youtube-logo.png" alt="YouTube Logo" className="logo" />
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <input
                            type="text"
                            id="username"
                            placeholder="Username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            autoComplete="off"
                        />
                    </div>
                    <div className="form-group">
                        <input
                            type={showPassword ? 'text' : 'password'}
                            id="password"
                            placeholder="Password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            autoComplete="off"
                        />
                    </div>
                    <div className="show-password-checkbox">
                        <input
                            type="checkbox"
                            id="show-password"
                            checked={showPassword}
                            onChange={() => setShowPassword(!showPassword)}
                        />
                        <label htmlFor="show-password">Show Password</label>
                    </div>
                    {error && <p className="error">{error}</p>}
                    <button type="submit">Login</button>
                    <button type="button" className="signup-button" onClick={handleSignUp}>Sign Up</button>
                </form>
                <div className="login-message">
                    Username must be at least 5 characters long and has to contain at least 1 number and 1 letter<br />
                    Password must be at least 8 characters long and has to contain at least 1 number and 1 letter
                </div>
            </div>
        </div>
    );
}

export default Login;
