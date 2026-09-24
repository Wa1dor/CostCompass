import { useState, useEffect } from 'react'
import { Card } from './Card'
import type { Resources } from '../types'
import { apiGet } from '../api'

export function ResourcesList() {
  const [items, setItems] = useState<Resources[]>([])

  const loadResources = () => { apiGet<Resources[]>('resources').then(setItems) }

  useEffect(() => {
    loadResources()
  }, [])

  return (
    <div className="space-y-3">
      <h2 className="text-2xl font-bold text-slate-900">Resources</h2>
      <p className="text-sm text-slate-500 mt-1">Resurser som kan tilldelas till tasks, mänskliga eller AI-styrda</p>
      <div className="grid grid-cols-3 gap-4">
        {items.map(item => (
          <Card key={item.id}>
            <div className="flex items-start justify-between">
              <p className="text-sm font-semibold text-slate-900">{item.roles[0]?.name}</p>
              <span className={
                item.type === 'HUMAN'
                  ? 'text-xs font-semibold px-2.5 py-1 rounded-full uppercase tracking-wide bg-emerald-50 text-emerald-700'
                  : 'text-xs font-semibold px-2.5 py-1 rounded-full uppercase tracking-wide bg-violet-50 text-violet-700'
              }>
                {item.type === 'HUMAN' ? 'Human' : 'AI-directed'}
              </span>
            </div>
            {item.roles.map(role => (
              <span
                key={role.id}
                className="text-xs font-medium text-slate-700 bg-slate-100 px-2.5 py-1 rounded-full"
              >
                {role.name}
              </span>
            ))}

            <div className="flex items-baseline justify-between pt-2.5 mt-2 border-t border-slate-100">
              <span className="text-xs text-slate-500">Timpris</span>
              <span className="text-base font-bold text-slate-900">{item.hourlyPrice} kr/h</span>
            </div>
          </Card>
        ))}
      </div>
    </div>
  )
}