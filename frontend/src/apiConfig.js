/** Spring Boot API origin (no trailing slash). Set VUE_APP_API_BASE_URL on Render static site build. */
export const API_BASE_URL = (process.env.VUE_APP_API_BASE_URL || 'http://localhost:8081').replace(
  /\/$/,
  ''
);
