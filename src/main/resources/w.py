
import weaviate
import requests
import json
import weaviate.classes as wvc

fname = "jeopardy_tiny_with_vectors_all-OpenAI-ada-002.json"  # This file includes pre-generated vectors
url = f"https://raw.githubusercontent.com/weaviate-tutorials/quickstart/main/data/{fname}"
resp = requests.get(url)
data = json.loads(resp.text)  # Load data
question_objs = list()
for i, d in enumerate(data):
    question_objs.append(wvc.data.DataObject(
        properties={
            "answer": d["Answer"],
            "question": d["Question"],
            "category": d["Category"],
        },
        vector=d["vector"]
    ))

client = weaviate.connect_to_local()
print(f"connection status: {client.is_ready()}")

questions = client.collections.get("Question")
questions.data.insert_many(question_objs)

# close connection
client.close()

