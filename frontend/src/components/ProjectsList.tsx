import { useEffect, useState } from "react";
import type { Projects } from "../types";
import { Card } from "./Card";
import { apiGet } from "../api";
import { Link } from "react-router";

export function ProjectsList() {
  const [items, setItems] = useState<Projects[]>([])

  const loadProjects = () => { apiGet<Projects[]>('projects').then(setItems) }

  useEffect(() => {
    loadProjects()
  }, [])

  return (
    <div className="space-y-3">
      <h2 className="text-2xl font-bold text-slate-900">Projects</h2>
      <p className="text-sm text-slate-500 mt-1">Kundprojects med tilldelade tasks</p>
      {items.map(item => (
        <Card key={item.id}>
          <div className="flex items-center justify-between gap-5">
            <div>
              <p className="text-xs font-semibold text-indigo-600 uppercase tracking-wide">{item.customer}</p>
              <p className="text-base font-bold text-slate-900 mt-0.5">{item.projectName}</p>
              <p className="text-sm text-slate-500 mt-0.5">{item.tasks.length} tasks tilldelade</p>
            </div>
            <Link key={item.id} to={`/projects/${item.id}`}>
              <p className="text-sm font-semibold text-indigo-600 bg-indigo-50 px-4 py-2 rounded-lg">View tasks →</p>
            </Link>
          </div>
        </Card>
      ))
      }
    </div >
  )
}