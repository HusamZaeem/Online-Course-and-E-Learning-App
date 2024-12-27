const express = require('express');
const nodemailer = require('nodemailer');
require('dotenv').config(); // Ensure you load environment variables from a .env file

const app = express();
app.use(express.json());

const transporter = nodemailer.createTransport({
  service: 'gmail',
  auth: {
    user: process.env.EMAIL_USER,  // Environment variable for the email
    pass: process.env.EMAIL_PASS   // Environment variable for the app password
  }
});

// Endpoint to send OTP
app.post('/send-otp', async (req, res) => {
  const { email, otp } = req.body;

  const mailOptions = {
    from: process.env.EMAIL_USER,  // Using environment variable for 'from' address
    to: email,
    subject: 'Your OTP Code',
    text: `Your OTP code is: ${otp}`
  };

  try {
    await transporter.sendMail(mailOptions);
    res.status(200).send('OTP sent successfully!');
  } catch (error) {
    res.status(500).send(`Failed to send OTP: ${error.message}`);
  }
});

// Start the server
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});
