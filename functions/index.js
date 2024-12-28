const express = require('express');
const cors = require('cors');
const nodemailer = require('nodemailer');
require('dotenv').config();

const app = express();

// Enable CORS for all origins
app.use(cors());

app.use(express.json());

// Set a global timeout for all requests (in milliseconds, 60 seconds here)
app.use((req, res, next) => {
  res.setTimeout(60000, () => {  // 60 seconds timeout
    res.status(408).send('Request timed out.');
  });
  next();
});

const transporter = nodemailer.createTransport({
  service: 'gmail',
  auth: {
    user: process.env.EMAIL_USER, // Use your Gmail email
    pass: process.env.EMAIL_PASS  // Your Gmail app password
  },
  // Optionally, you can increase the connection timeout for the email transporter.
  connectionTimeout: 60000,  // Timeout for the SMTP connection (in milliseconds)
  greetingTimeout: 60000,    // Timeout for the initial greeting (in milliseconds)
  socketTimeout: 60000       // Timeout for reading from or writing to the socket (in milliseconds)
});

// Endpoint to send OTP
app.post('/send-otp', async (req, res) => {
  const { email, otp } = req.body;

  const mailOptions = {
    from: process.env.EMAIL_USER, // Sender's email
    to: email,  // Recipient's email
    subject: 'Your OTP for Password Reset',
    text: `Dear User,\n\nYour OTP for password reset is: ${otp}.\n\nPlease do not share this code with anyone.\n\nBest regards,\nOnline Course & E-Learning App`
  };

  try {
    await transporter.sendMail(mailOptions);
    res.status(200).send('OTP sent successfully!');
  } catch (error) {
    res.status(500).send(`Failed to send OTP: ${error.message}`);
  }
});

// Start the server
const PORT = process.env.PORT || 10000;
app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});
