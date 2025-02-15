class UserService {
  static API_URL = "http://localhost:8080/api/v1/create";

  static async createUser(username) {
    if (!username.trim()) return { error: "Username cannot be empty" };

    try {
      const response = await fetch(this.API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username }),
      });

      const data = await response.json();

      if (!response.ok) {
        return { error: data || "Failed to create user" };
      }

      return { user: data };
    } catch (error) {
      console.error("Error:", error);
      return { error: "Something went wrong" };
    }
  }
}

export default UserService;
