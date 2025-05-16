import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Layout } from './components/Layout';
import { Home } from './pages/Home';
import { Tasks } from './pages/tasks/Tasks.tsx';
import { TaskDetail } from './pages/tasks/TaskDetail.tsx';
import './App.css'
import {AuthProvider} from "./context/AuthContext.tsx";
import {Unauthorized} from "./pages/Unauthorized.tsx";
import {AdminRoute} from "./routes/AdminRoute.tsx";
import {AdminPage} from "./pages/admin/AdminPage.tsx";
import {Login} from "./pages/Login.tsx";
import {MemberRoute} from "./routes/MemberRoute.tsx";

function App() {
  return (
      <AuthProvider>
        <Router>
          <Layout>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/tasks" element={
                    <MemberRoute>
                        <Tasks />
                    </MemberRoute>
                }
                />
                <Route path="/tasks/:id" element={
                    <MemberRoute>
                        <TaskDetail />
                    </MemberRoute>
                } />
                <Route path="/unauthorized" element={ <Unauthorized/>} />
                <Route path="/login" element={ <Login/> } />
                <Route path="/admin" element={
                   <AdminRoute>
                        <AdminPage/>
                   </AdminRoute>
                }/>
            </Routes>
          </Layout>
        </Router>

      </AuthProvider>
  );
}

export default App;
