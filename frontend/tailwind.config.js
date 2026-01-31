/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      colors: {
        padel: {
          navy: '#0f172a',    // Dark Navy
          green: '#84cc16',   // Lime Green (Padel ball)
          teal: '#14b8a6',    // Teal accent
          dark: '#1e293b',    // Slate 800
          light: '#f1f5f9',   // Slate 100
        }
      },
      fontFamily: {
        sans: ['Inter', 'system-ui', 'sans-serif'],
      }
    },
  },
  plugins: [],
}
