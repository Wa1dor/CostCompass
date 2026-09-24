import { useEffect, useState } from "react";
import type { Projects } from "../types";
import { Link, useParams } from "react-router";
import { apiGet } from "../api";
import { Card } from "./Card";

export function ProjectDetail() {
  const { id } = useParams()
  const [project, setProject] = useState<Projects | null>(null)

  useEffect(() => {
    apiGet<Projects[]>('projects').then(projects => {
      setProject(projects.find(p => p.id === Number(id)) ?? null)
    })
  }, [id])
  if (!project) {
    return <p className="text-sm text-slate-500">Laddar...</p>
  }

  return (
    <div className="space-y-3">
      <Link to="/projects" className="text-sm font-semibold text-indigo-600">
        ← Tillbaka till projects
      </Link>

      <div>
        <p className="text-xs font-semibold text-indigo-600 uppercase tracking-wide">{project.customer}</p>
        <h2 className="text-2xl font-bold text-slate-900">{project.projectName}</h2>
        <p className="text-sm text-slate-500 mt-1">{project.tasks.length} tasks · {project.price} kr totalt</p>
      </div>

      <div className="space-y-3 mt-4">
        {project.tasks.map(task => (
          <Card key={task.id}>
            <div className="flex items-center justify-between gap-5">
              <div>
                <p className="font-semibold text-slate-900">{task.name}</p>
                <p className="text-sm text-slate-500">{task.description}</p>
              </div>
              <div className="flex items-center gap-6">
                <div className="text-right">
                  <p className="text-sm font-semibold text-slate-700">{task.resource.roles[0]?.name}</p>
                  <p className="text-xs text-slate-400">{task.resource.type}</p>
                </div>
                <p className="text-base font-bold text-slate-900">{task.price} kr</p>
              </div>
            </div>
          </Card>
        ))}
      </div>
    </div>
  )
}