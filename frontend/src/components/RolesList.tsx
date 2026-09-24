import { useState, useEffect } from 'react'
import { Card } from './Card'
import type { Roles } from '../types'
import { apiGet } from '../api'

export function RolesList() {
  const [items, setItems] = useState<Roles[]>([])

  const loadRoles = () => apiGet<Roles[]>('roles').then(setItems)

  useEffect(() => {
    loadRoles()
  }, [])

  async function handleDelete(id: number) {
    if (!confirm('Delete this Roles')) return
    await fetch(`http://localhost:8080/api/costcompass/roles/${id}`, {
      method: 'DELETE',
    })
    loadRoles()
  }

  return (
    <div className="space-y-3">
      <h2 className="text-2xl font-bold text-slate-900">Roles</h2>
      <p className="text-sm text-slate-500 mt-1">Definierade roller som resurser kan tilldelas</p>

      {items.map(item => (
        <Card key={item.id}>
          <div className="flex items-center justify-between">
            <div>
              <p className="font-medium text-slate-900">{item.name}</p>
              <p className="text-slate-500 text-sm">{item.description}</p>
            </div>
            <span className="text-xs font-medium text-slate-700 bg-slate-100 px-2.5 py-1 rounded-full">
              Roll
            </span>
            <button
              className='text-xs font-medium text-red-600 hover:bg-red-50 px-2.5 py-1 rounded-full'
              onClick={() => handleDelete(item.id)}>
              Delete
            </button>
          </div>
        </Card>
      ))
      }
    </div >
  )
}