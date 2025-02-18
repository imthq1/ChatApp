class UserService {
  static API_URL_CREATE = "http://localhost:8080/api/v1/create";
  static API_URL_GETID = "http://localhost:8080/api/v1/getUser";

  static async createUser(username) {
    if (!username.trim()) return { error: "Username cannot be empty" };
    const status = "ONLINE";
    try {
      const response = await fetch(this.API_URL_CREATE, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, status }),
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
  static async getId(username) {
    if (!username.trim()) return { error: "Username cannot be empty" };

    try {
      const response = await fetch(
        `${this.API_URL_GETID}?username=${username}`,
        {
          method: "GET", // Sử dụng GET thay vì POST
        }
      );

      const data = await response.json();

      if (!response.ok) {
        return { error: data || "Failed to get user ID" };
      }

      return { user: data };
    } catch (error) {
      console.error("Error:", error);
      return { error: "Something went wrong" };
    }
  }
}

export default UserService;
