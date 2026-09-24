import { useEffect, useState } from "react";
import type { Tasks } from "../types";
import { Card } from "./Card";
import { apiGet } from "../api";

export function TasksList() {
  const [items, setItems] = useState<Tasks[]>([])

  const loadTasks = () => apiGet<Tasks[]>('tasks').then(setItems)

  useEffect(() => {
    loadTasks()
  }, [])

  return (
    <div className="space-y-3">
      <h2 className="text-2xl font-bold text-slate-900">Tasks</h2>
      <p className="text-sm text-slate-500 mt-1">Arbetsuppgifter med tilldelad resurs och beräknat pris</p>

      {items.map(item => (
        <Card key={item.id}>
          <div className="flex items-center justify-between gap-5">
            <div>
              <p className="font-semibold text-slate-900">{item.name}</p>
              <p className="text-sm text-slate-500">{item.description}</p>
            </div>
            <div className="flex items-center gap-6">
              <div className="text-right">
                <p className="text-sm font-semibold text-slate-700">{item.resource.roles[0]?.name}</p>
                <p className="text-xs text-slate-400">{item.resource.type}</p>
              </div>
              <p className="text-base font-bold text-slate-900">{item.price} kr</p>
            </div>
          </div>
        </Card>
      ))}
    </div>
  )
}