import { useEffect, useState, useRef } from "react"
import type { Projects } from "../types"
import Markdown from "react-markdown"
import remarkBreaks from "remark-breaks"
import { apiGet } from "../api"

type Message = {
  id: number
  sender: "USER" | "ASSISTANT"
  text: string
}

export function AiChat() {
  const [projects, setProjects] = useState<Projects[]>([])
  const [selectedProject, setSelectedProject] =
    useState<Projects | null>(null)

  const [input, setInput] = useState("")
  const [messages, setMessages] = useState<Message[]>([])

  // Change textbox size
  const textareaRef = useRef<HTMLTextAreaElement>(null)

  const resizeTextarea = () => {
    const el = textareaRef.current
    if (!el) return
    el.style.height = "auto"
    el.style.height = Math.min(el.scrollHeight, 160) + "px" // 160px ≈ tak, justera efter smak
  }

  const loadProjects = () => {
    apiGet<Projects[]>('projects').then(setProjects).catch(error => {
      console.error("Kunde inte hämta projekt:", error)
    })
  }

  useEffect(() => {
    resizeTextarea()
  }, [input])

  // Hämta projekt
  useEffect(() => {
    loadProjects()
  }, [])

  async function sendMessage(text: string, projectId: number | null) {
    if (!text.trim()) return
    const userMessage: Message = { id: Date.now(), sender: "USER", text }
    setMessages(prev => [...prev, userMessage])
    setInput("")
    const response = await fetch("http://localhost:8080/api/costcompass/assistant/request", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        message: text,
        history: messages.map(m => ({ sender: m.sender, text: m.text })),
        projectId,
      }),
    })
    const data = await response.json()
    console.log(data.action)
    if (data.action != null) loadProjects()
    setMessages(prev => [...prev, { id: Date.now(), sender: "ASSISTANT", text: data.reply }])
  }

  // Använd förslag
  const useSuggestion = (text: string) => {
    setInput(text)
  }

  return (
    <div className="h-[calc(100vh-64px)] flex bg-slate-50">
      {/* ================= SIDEBAR ================= */}
      <aside className="w-52 bg-white border-r border-slate-200 p-3">
        {/* Nytt projekt */}
        <button className=" w-full bg-indigo-600 hover:bg-indigo-700
            text-white text-xs transition py-2 rounded-lg font-medium"
          onClick={() => { setSelectedProject(null); sendMessage("Skapa ett nytt project", null) }}>
          + Nytt projekt
        </button>

        {/* Pågående projekt */}
        <div className="mt-5">
          <p className="px-2 text-[10px] font-semibold text-slate-400 uppercase tracking-wide">
            Pågående projekt
          </p>
          <div className="mt-2 space-y-1">
            {projects.map(project => {
              const selected =
                selectedProject?.id === project.id
              return (
                <button
                  key={project.id}
                  onClick={() => setSelectedProject(project)}
                  className={`w-full text-left px-2
                    py-2 rounded-lg transition
                    ${selected
                      ? "bg-indigo-50 text-indigo-600"
                      : "text-slate-700 hover:bg-slate-50"
                    }
                  `}>

                  <p className="text-xs font-medium">
                    {project.customer}
                  </p>

                  <p className="text-[10px] text-slate-400">
                    {project.projectName}
                  </p>
                </button>
              )
            })}
          </div>
        </div>

        {/* ================= FÖRSLAG ================= */}
        <div className="mt-5">
          <p className="px-2 text-[10px] font-semibold text-slate-400 uppercase tracking-wide">
            Förslag
          </p>
          <div className="mt-2 space-y-1">
            <button
              onClick={() =>
                useSuggestion(
                  "Vilken resurs passar bäst till DevOps?"
                )
              }
              className="w-full text-left px-2
                py-2 border border-slate-200
                rounded-lg text-[11px] text-slate-600 hover:bg-slate-50">
              Vilken resurs passar bäst till DevOps?
            </button>
            <button
              onClick={() =>
                useSuggestion(
                  "Sammanfatta kostnaden hittills"
                )
              }
              className="w-full text-left px-2 py-2
                border border-slate-200 rounded-lg
                text-[11px] text-slate-600 hover:bg-slate-50">
              Sammanfatta kostnaden hittills
            </button>
          </div>
        </div>
      </aside>

      {/* ================= CHAT ================= */}
      <main className="flex-1 flex flex-col min-w-0">

        {/* Projekt-header */}
        <div className="bg-white border-b border-slate-200 px-6 py-4">
          <h2 className="text-sm font-semibold text-slate-900">
            {selectedProject?.projectName ?? "Välj ett projekt"}
          </h2>
          <p className="text-[10px] text-slate-400">
            {selectedProject?.customer ?? ""}
          </p>
        </div>

        {/* ================= MEDDELANDEN ================= */}
        <div className="flex-1 overflow-y-auto px-8 py-6">
          <div className="max-w-3xl mx-auto space-y-5">
            {messages.length === 0 && (
              <div className="text-center mt-20">
                <p className="text-sm font-medium text-slate-500">
                  Kostnadsassistent
                </p>
                <p className="text-xs text-slate-400 mt-1">
                  Ställ en fråga om kostnader, resurser eller tasks.
                </p>
              </div>
            )}
            {messages.map(message => (
              <div
                key={message.id}
                className={`
                  flex
                  ${message.sender === "USER"
                    ? "justify-end"
                    : "justify-start"
                  }
                `}>
                {/* USER */}
                {message.sender === "USER" && (
                  <div
                    className=" max-w-xl bg-indigo-600 text-white
                      rounded-xl rounded-br-md px-4
                      py-3 text-xs leading-relaxed">
                    <Markdown remarkPlugins={[remarkBreaks]}>
                      {message.text}
                    </Markdown>
                  </div>
                )}

                {/* ASSISTANT */}
                {message.sender === "ASSISTANT" && (
                  <div
                    className="max-w-xl bg-white border
                      border-slate-200 rounded-xl rounded-bl-md
                      px-4 py-3 shadow-sm">
                    <p className="text-[9px] uppercase font-semibold text-indigo-500 mb-1">
                      Kostnadsassistent
                    </p>
                    <div className="text-xs text-slate-700 leading-relaxed">
                      <Markdown>
                        {message.text}
                      </Markdown>
                    </div>
                  </div>
                )}
              </div>
            ))}
          </div>
        </div>
        <div className="px-8 pb-5">
          <div
            className="max-w-3xl mx-auto bg-white
                    border border-slate-200 rounded-xl
                    shadow-sm p-2 flex gap-2">
            <textarea
              ref={textareaRef}
              value={input}
              onChange={e => setInput(e.target.value)}
              onKeyDown={e => {
                if (e.key === "Enter" && !e.shiftKey) {
                  e.preventDefault()
                  sendMessage(input, selectedProject?.id ?? null)
                }
              }}
              placeholder="Skriv ett meddelande..."
              className=" flex-1 px-3 py-2
                        text-xs text-slate-700 outline-none
                        placeholder:text-slate-300"/>
            <button
              onClick={() => sendMessage(input, selectedProject?.id ?? null)}
              className="px-4  py-2 rounded-lg
                      bg-indigo-600 hover:bg-indigo-700 text-white
                      text-xs font-medium transition">
              Skicka
            </button>
          </div>
        </div>
      </main>
    </div>
  )
}