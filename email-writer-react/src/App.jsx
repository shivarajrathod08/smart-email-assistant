import { useState } from 'react';
import './App.css';
import {
  Box, Button, CircularProgress, Container,
  FormControl, InputLabel, MenuItem, Select,
  TextField, Typography
} from '@mui/material';
import axios from 'axios';

function App() {
  const [emailContent, setEmailContent] = useState('');
  const [tone, setTone] = useState('');
  const [generatedReply, setGeneratedReply] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  // Backend URL from Vite environment variables
  const backendURL = import.meta.env.VITE_API_URL;

  const handleSubmit = async () => {
    if (!backendURL) {
      setError('Backend URL is not configured.');
      return;
    }

    setLoading(true);
    setError('');
    setGeneratedReply('');

    try {
      const response = await axios.post(
        `${backendURL}/api/email/generate`,
        { originalEmailContent: emailContent, tone },
        { withCredentials: true }
      );

      setGeneratedReply(
        typeof response.data === 'string'
          ? response.data
          : JSON.stringify(response.data)
      );
    } catch (err) {
      console.error(err);
      setError('Failed to generate email reply. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container maxWidth="md" sx={{ py: 4 }}>
      <Typography variant='h3' component="h1" gutterBottom>
        Email Reply Generator
      </Typography>

      <Box sx={{ mx: 3 }}>
        {/* Original Email Content */}
        <TextField
          id="emailContent"
          name="emailContent"
          label="Original Email Content"
          autoComplete="off"
          fullWidth
          multiline
          rows={6}
          variant='outlined'
          value={emailContent}
          onChange={(e) => setEmailContent(e.target.value)}
          sx={{ mb: 2 }}
        />

        {/* Tone Selection */}
        <FormControl fullWidth sx={{ mb: 2 }}>
          <InputLabel id="tone-label">Tone (Optional)</InputLabel>
          <Select
            id="tone"
            name="tone"
            labelId="tone-label"
            value={tone}
            label="Tone (Optional)"
            onChange={(e) => setTone(e.target.value)}
          >
            <MenuItem value="">None</MenuItem>
            <MenuItem value="professional">Professional</MenuItem>
            <MenuItem value="casual">Casual</MenuItem>
            <MenuItem value="friendly">Friendly</MenuItem>
          </Select>
        </FormControl>

        {/* Generate Button */}
        <Button
          variant='contained'
          onClick={handleSubmit}
          disabled={!emailContent || loading}
          fullWidth
        >
          {loading ? <CircularProgress size={24} /> : "Generate Reply"}
        </Button>
      </Box>

      {/* Error Message */}
      {error && (
        <Typography color='error' sx={{ mt: 2 }}>
          {error}
        </Typography>
      )}

      {/* Generated Reply */}
      {generatedReply && (
        <Box sx={{ mt: 3 }}>
          <TextField
            id="generatedReply"
            name="generatedReply"
            label="Generated Reply"
            value={generatedReply}
            InputProps={{ readOnly: true }}
            fullWidth
            multiline
            rows={6}
            variant='outlined'
          />
          <Button
            variant='outlined'
            sx={{ mt: 2 }}
            onClick={() => navigator.clipboard.writeText(generatedReply)}
          >
            Copy to Clipboard
          </Button>
        </Box>
      )}
    </Container>
  );
}

export default App;