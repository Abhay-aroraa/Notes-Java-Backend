A full-stack notes application built with React (Vite) frontend, Spring Boot backend, and MongoDB Atlas for persistent storage.
It allows users to create, edit, pin, archive, delete, restore, and search notes — a lightweight clone of Google Keep.

🚀 Features

✍️ Add / Edit Notes – Create and update notes in real time

📌 Pin / Unpin Notes – Keep important notes on top

🗄 Archive Notes – Hide notes without deleting them

🗑 Trash / Restore Notes – Recover deleted notes or delete permanently

🔍 Search Notes – Quickly find notes by keywords

🎨 Responsive UI – Mobile-friendly design

🛡 Authentication (Planned) – Secure login, signup, password reset with JWT or Firebase

🏗 Tech Stack
Frontend (React + Vite)

⚛️ React.js

🎨 Tailwind CSS

🔄 Axios (API calls)

Backend (Spring Boot)

☕ Java + Spring Boot

🗄 MongoDB Atlas (Cloud Database)

📦 RESTful APIs

📂 Project Structure
notes-app/
│── backend/ (Spring Boot)
│   ├── model/          # Note model (fields: id, title, content, pinned, archived, trashed, etc.)
│   ├── service/        # Business logic for notes
│   ├── controller/     # REST controllers (APIs for notes)
│   └── repository/     # MongoDB interface
│
│── frontend/ (React + Vite)
│   ├── src/
│   │   ├── pages/      # Home, View, AddNote
│   │   ├── components/ # NoteCard, SearchBar, Navbar
│   │   ├── api/        # Axios API calls
│   │   └── App.jsx
│   └── public/
│
└── README.md
