export interface Roles {
  id: number
  name: string
  description: string
}

export interface Resources {
  id: number
  roles: Roles[]
  hourlyPrice: number
  type: string
}

export interface Tasks {
  id: number
  name: string
  description: string
  resource: Resources
  estimatedExecutionTime: string
  estimatedVerificationTime: string
  price: number
}

export interface Projects {
  id: number
  customer: string
  projectName: string
  tasks: Tasks[]
  price: number
}

export interface Memories {
  id: number
  content: string
  category: string
  createdBy: string
  createdAt: string
  updatedAt: string | null
}