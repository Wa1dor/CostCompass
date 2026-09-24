import { useEffect, useState, useRef } from "react"
import Markdown from "react-markdown"
import remarkBreaks from "remark-breaks"
import type { Memories } from "../types"
import { apiGet } from "../api"

type Message = {
  id: number
  sender: "USER" | "ASSISTANT"
  text: string
}

const CATEGORY_STYLES: Record<string, string> = {
  TASK: "bg-indigo-50 text-indigo-600",
  DECISION: "bg-purple-50 text-purple-600",
  BUG: "bg-red-50 text-red-600",
  NOTE: "bg-slate-100 text-slate-500",
  ARCHITECTURE: "bg-amber-50 text-amber-600",
}

export function DevAiChat() {
  const [input, setInput] = useState("")
  const [messages, setMessages] = useState<Message[]>([])
  const [memories, setMemories] = useState<Memories[]>([])

  const textareaRef = useRef<HTMLTextAreaElement>(null)

  const resizeTextarea = () => {
    const el = textareaRef.current
    if (!el) return
    el.style.height = "auto"
    el.style.height = Math.min(el.scrollHeight, 160) + "px"
  }

  const loadMemories = () => {
    apiGet<Memories[]>('dev/memory')
      .then(data => setMemories([...data].sort((a, b) => b.id - a.id)))
      .catch(error => {
        console.error("Kunde inte hämta minnen:", error)
      })
  }

  useEffect(() => {
    resizeTextarea()
  }, [input])

  useEffect(() => {
    loadMemories()
  }, [])

  async function sendMessage(text: string) {
    if (!text.trim()) return
    const userMessage: Message = { id: Date.now(), sender: "USER", text }
    setMessages(prev => [...prev, userMessage])
    setInput("")
    const response = await fetch("http://localhost:8080/api/costcompass/dev/assistant/request", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        message: text,
        history: messages.map(m => ({ sender: m.sender, text: m.text })),
      }),
    })
    const data = await response.json()
    setMessages(prev => [...prev, { id: Date.now(), sender: "ASSISTANT", text: data.reply }])
    loadMemories()
  }

  const useSuggestion = (text: string) => {
    setInput(text)
  }

  return (
    <div className="h-[calc(100vh-64px)] flex bg-slate-50">
      {/* ================= SIDEBAR ================= */}
      <aside className="w-64 bg-white border-r border-slate-200 p-3 overflow-y-auto">
        <p className="px-2 text-[10px] font-semibold text-slate-400 uppercase tracking-wide">
          Dev-assistent
        </p>
        <p className="px-2 mt-1 text-[10px] text-slate-400 leading-relaxed">
          Kommer ihåg tasks och beslut om costcompass-utvecklingen.
        </p>

        <div className="mt-5">
          <p className="px-2 text-[10px] font-semibold text-slate-400 uppercase tracking-wide">
            Förslag
          </p>
          <div className="mt-2 space-y-1">
            <button
              onClick={() => useSuggestion("Vad har vi bestämt hittills om projektet?")}
              className="w-full text-left px-2 py-2 border border-slate-200 rounded-lg text-[11px] text-slate-600 hover:bg-slate-50">
              Vad har vi bestämt hittills?
            </button>
            <button
              onClick={() => useSuggestion("Kom ihåg att: ")}
              className="w-full text-left px-2 py-2 border border-slate-200 rounded-lg text-[11px] text-slate-600 hover:bg-slate-50">
              Spara ett nytt minne
            </button>
          </div>
        </div>

        {/* ================= MINNEN ================= */}
        <div className="mt-5">
          <p className="px-2 text-[10px] font-semibold text-slate-400 uppercase tracking-wide">
            Sparade minnen ({memories.length})
          </p>
          <div className="mt-2 space-y-2">
            {memories.length === 0 && (
              <p className="px-2 text-[11px] text-slate-400">Inga minnen sparade än.</p>
            )}
            {memories.map(memory => (
              <div key={memory.id} className="px-2 py-2 rounded-lg border border-slate-200">
                <span className={`inline-block px-1.5 py-0.5 rounded text-[9px] font-semibold uppercase ${CATEGORY_STYLES[memory.category] ?? "bg-slate-100 text-slate-500"}`}>
                  {memory.category}
                </span>
                <p className="text-[11px] text-slate-600 mt-1 leading-snug">
                  {memory.content}
                </p>
              </div>
            ))}
          </div>
        </div>
      </aside>

      {/* ================= CHAT ================= */}
      <main className="flex-1 flex flex-col min-w-0">
        <div className="bg-white border-b border-slate-200 px-6 py-4">
          <h2 className="text-sm font-semibold text-slate-900">Dev-chat</h2>
          <p className="text-[10px] text-slate-400">Minns tasks och beslut om costcompass</p>
        </div>

        <div className="flex-1 overflow-y-auto px-8 py-6">
          <div className="max-w-3xl mx-auto space-y-5">
            {messages.length === 0 && (
              <div className="text-center mt-20">
                <p className="text-sm font-medium text-slate-500">Dev-assistent</p>
                <p className="text-xs text-slate-400 mt-1">
                  Fråga om tidigare beslut, eller be den komma ihåg något nytt.
                </p>
              </div>
            )}
            {messages.map(message => (
              <div
                key={message.id}
                className={`flex ${message.sender === "USER" ? "justify-end" : "justify-start"}`}>
                {message.sender === "USER" && (
                  <div className="max-w-xl bg-indigo-600 text-white rounded-xl rounded-br-md px-4 py-3 text-xs leading-relaxed">
                    <Markdown remarkPlugins={[remarkBreaks]}>{message.text}</Markdown>
                  </div>
                )}
                {message.sender === "ASSISTANT" && (
                  <div className="max-w-xl bg-white border border-slate-200 rounded-xl rounded-bl-md px-4 py-3 shadow-sm">
                    <p className="text-[9px] uppercase font-semibold text-indigo-500 mb-1">
                      Dev-assistent
                    </p>
                    <div className="text-xs text-slate-700 leading-relaxed">
                      <Markdown>{message.text}</Markdown>
                    </div>
                  </div>
                )}
              </div>
            ))}
          </div>
        </div>

        <div className="px-8 pb-5">
          <div className="max-w-3xl mx-auto bg-white border border-slate-200 rounded-xl shadow-sm p-2 flex gap-2">
            <textarea
              ref={textareaRef}
              value={input}
              onChange={e => setInput(e.target.value)}
              onKeyDown={e => {
                if (e.key === "Enter" && !e.shiftKey) {
                  e.preventDefault()
                  sendMessage(input)
                }
              }}
              placeholder="Skriv ett meddelande..."
              className="flex-1 px-3 py-2 text-xs text-slate-700 outline-none placeholder:text-slate-300" />
            <button
              onClick={() => sendMessage(input)}
              className="px-4 py-2 rounded-lg bg-indigo-600 hover:bg-indigo-700 text-white text-xs font-medium transition">
              Skicka
            </button>
          </div>
        </div>
      </main>
    </div>
  )
}