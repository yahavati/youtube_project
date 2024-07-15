// Connect to your MongoDB instance
const { MongoClient } = require("mongodb");

async function removeEmailIndex() {
  const uri = "mongodb://127.0.0.1:27017"; // Replace with your MongoDB connection string
  const client = new MongoClient(uri, {
    useNewUrlParser: true,
    useUnifiedTopology: true,
  });

  try {
    await client.connect();
    const database = client.db("Youtube"); // Replace with your database name
    const users = database.collection("users");

    // List indexes
    const indexes = await users.indexes();
    console.log("Current indexes:", indexes);

    // Check if an index on 'email' exists and drop it
    const emailIndex = indexes.find((index) => index.key.email);
    if (emailIndex) {
      await users.dropIndex(emailIndex.name);
      console.log("Dropped index:", emailIndex.name);
    } else {
      console.log("No index on 'email' found.");
    }
  } finally {
    await client.close();
  }
}

removeEmailIndex().catch(console.error);
