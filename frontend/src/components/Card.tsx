import type React from "react";

interface CardProps {
  children: React.ReactNode
}

export function Card({ children }: CardProps) {
  return (
    <div className="bg-white border border-slate-200 rounded-lg p-4">
      {children}
    </div>
  )
}