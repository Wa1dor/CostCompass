import React, { useEffect } from 'react'
import { RolesList } from './components/RolesList'
import { ResourcesList } from './components/ResourcesList'
import { TasksList } from './components/TasksList'
import { ProjectsList } from './components/ProjectsList'
import { DashboardList } from './components/DashboardList'
import { AiChat } from './components/AiChat'
import { Navigate, NavLink, Route, Routes, useLocation } from 'react-router'
import { ProjectDetail } from './components/ProjectDetail'
import { DevAiChat } from './components/DevAiChat'

const TABS = [
  { path: 'dashboard', label: 'Dashboard' },
  { path: 'roles', label: 'Roles' },
  { path: 'resources', label: 'Resources' },
  { path: 'tasks', label: 'Tasks' },
  { path: 'projects', label: 'Projects' },
  { path: 'ai-chat', label: 'AI Chat' },
  { path: 'dev-ai-chat', label: 'Dev AI Chat' },
]

function PageLayout({ children }: { children: React.ReactNode }) {
  return <div className='max-w-5xl mx-auto px-6 py-8'>{children}</div>
}

function ScrollToTop() {
  const { pathname } = useLocation()
  useEffect(() => {
    window.scrollTo(0, 0)
  }, [pathname])
  return null
}
function App() {
  return (
    <div>
      <ScrollToTop />

      <div className="flex items-center justify-between px-6 h-16 bg-white border-b border-slate-200">
        <div className='text-lg font-bold text-slate-900'>
          Cost<span className="text-indigo-600">Compass</span>
        </div>
        <nav className='flex gap-1'>
          {TABS.map(tab => (
            <NavLink
              key={tab.path}
              to={tab.path}
              className={({ isActive }) =>
                isActive
                  ? 'px-4 py-2 rounded-lg text-sm font-medium bg-indigo-50 text-indigo-600'
                  : 'px-4 py-2 rounded-lg text-sm font-medium text-slate-500'
              }>
              {tab.label}
            </NavLink>
          ))}
        </nav>

      </div>
      <Routes>
        <Route path='' element={<Navigate to="/DashboardList" replace />} />
        <Route path='dashboard' element={<PageLayout><DashboardList></DashboardList></PageLayout>} />
        <Route path='roles' element={<PageLayout><RolesList></RolesList></PageLayout>} />
        <Route path='resources' element={<PageLayout><ResourcesList></ResourcesList></PageLayout>} />
        <Route path='tasks' element={<PageLayout><TasksList></TasksList></PageLayout>} />
        <Route path='projects' element={<PageLayout><ProjectsList></ProjectsList></PageLayout>} />
        <Route path='projects/:id' element={<PageLayout><ProjectDetail></ProjectDetail></PageLayout>} />
        <Route path='ai-chat' element={<AiChat></AiChat>} />
        <Route path='dev-ai-chat' element={<DevAiChat></DevAiChat>} />
      </Routes>
    </div>

  )
}

export default App