import { useEffect, useState } from "react";
import type { Projects, Resources, Roles, Tasks } from "../types";
import { Card } from "./Card";

function useData<T>(path: string, setData: React.Dispatch<React.SetStateAction<T>>) {
  return useEffect(() => {
    fetch('http://localhost:8080/api/costcompass/' + path)
      .then(response => response.json())
      .then(data => setData(data))
  }, [])
}

export function DashboardList() {
  //** Placeholder just nu  */
  const [resources, setResources] = useState<Resources[]>([])
  const [projects, setProjects] = useState<Projects[]>([])
  const [tasks, setTasks] = useState<Tasks[]>([])
  const [roles, setRoles] = useState<Roles[]>([])

  const humanResources = resources.filter(
    resource => resource.type === "HUMAN"
  ).length

  const aiResources = resources.filter(
    resource => resource.type === "AI_DIRECTED"
  ).length

  const totalResources = resources.length

  const humanPercentage =
    totalResources > 0
      ? (humanResources / totalResources) * 100
      : 0

  const aiPercentage =
    totalResources > 0
      ? (aiResources / totalResources) * 100
      : 0

  useData("resources", setResources)
  useData("projects", setProjects)
  useData("tasks", setTasks)
  useData("roles", setRoles)
  const totalCost = projects.reduce((sum, project) => sum + project.price, 0)
  const assignedTasks = projects.reduce((sum, project) => sum + project.tasks.length, 0)

  return (
    <div className="space-y-3">
      <h2 className="text-2xl font-bold text-slate-900">Dashboard</h2>
      <p className="text-sm text-slate-500 mt-1">Överblick över projekt, tasks och resurser</p>
      <div className="grid grid-cols-4 gap-4">
        <Card>
          <p className="text-xs font-semibold text-slate-500 uppercase tracking-wide">Aktiva projekt</p>
          <p className="text-2xl font-bold text-slate-900 mt-2">{projects.length}</p>
          <p className="text-xs text-slate-400 mt-1">{assignedTasks} tasks tilldelade totalt</p>
        </Card>
        <Card>
          <p className="text-xs font-semibold text-slate-500 uppercase tracking-wide">Tasks</p>
          <p className="text-2xl font-bold text-slate-900 mt-2">{tasks.length}</p>
          <p className="text-xs text-slate-400 mt-1">definerade arbetsuppgifter</p>
        </Card>
        <Card>
          <p className="text-xs font-semibold text-slate-500 uppercase tracking-wide">Resuser</p>
          <p className="text-2xl font-bold text-slate-900 mt-2">{resources.length}</p>
          <p className="text-xs text-slate-400 mt-1">{humanResources} human - {aiResources} AI-directed</p>
        </Card>
        <Card>
          <p className="text-xs font-semibold text-slate-500 uppercase tracking-wide">Estimerad Kostnad</p>
          <p className="text-2xl font-bold text-slate-900 mt-2">{totalCost}</p>
          <p className="text-xs text-slate-400 mt-1">Summa av all projects</p>
        </Card>
      </div>
      <div className="grid grid-cols-2 gap-4">
        <Card>
          <p className="text-sm font-bold text-slate-900">Projekt</p>

          <div className="mt-4 divide-y divide-slate-100">
            {projects.map(project => (
              <div
                key={project.id}
                className="flex items-center justify-between py-3"
              >
                <div>
                  <p className="text-xs font-semibold text-indigo-500 uppercase">
                    {project.customer}
                  </p>

                  <p className="text-sm font-medium text-slate-900">
                    {project.projectName}
                  </p>
                </div>

                <p className="text-xs text-slate-500">
                  {project.tasks?.length ?? 0} tasks
                </p>
              </div>
            ))}
          </div>
        </Card>

        <Card>
          <p className="text-sm font-bold text-slate-900">
            Resursmix
          </p>

          <div className="mt-4 space-y-4">

            <div>
              <div className="flex justify-between mb-1">
                <p className="text-sm font-medium text-slate-700">
                  Human
                </p>

                <p className="text-sm font-semibold text-slate-900">
                  {humanResources}
                </p>
              </div>

              <div className="h-2 bg-slate-100 rounded-full">
                <div
                  className="h-2 bg-emerald-500 rounded-full"
                  style={{ width: `${humanPercentage}%` }}
                />
              </div>
            </div>

            <div>
              <div className="flex justify-between mb-1">
                <p className="text-sm font-medium text-slate-700">
                  AI-directed
                </p>

                <p className="text-sm font-semibold text-slate-900">
                  {aiResources}
                </p>
              </div>

              <div className="h-2 bg-slate-100 rounded-full">
                <div
                  className="h-2 bg-violet-600 rounded-full"
                  style={{ width: `${aiPercentage}%` }}
                />
              </div>
            </div>

          </div>
        </Card>
      </div>
    </div>

  )
}