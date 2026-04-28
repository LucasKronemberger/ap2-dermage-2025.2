## 🧴 Dermage Skin Analysis App
Este aplicativo foi desenvolvido como um projeto acadêmico para a Dermage em colaboração com a instituição Ibmec. O objetivo é oferecer uma consultoria de skincare digital e personalizada através de um questionário inteligente e análise facial via IA.

## 🎯 O Projeto
O app guia o usuário por uma jornada de diagnóstico de pele dividida em três etapas principais:
1. Questionário Interativo: Coleta de dados sobre rotina, faixa etária, sensibilidade e preocupações específicas do usuário.
2. Captura de Imagem: Através da câmera do dispositivo, o app captura uma foto do rosto do usuário.
3. Análise por IA: A imagem e os dados do quiz são enviados para uma API que processa as informações via Inteligência Artificial, retornando um diagnóstico detalhado e sugestões de produtos Dermage ideais para o perfil identificado.

## 🛠️ Tecnologias e Bibliotecas
- Linguagem: Kotlin (Nativo Android).
- Networking: Retrofit 2 para consumo da API REST.
- Arquitetura: Baseada em Activities para navegação de fluxo.
- Gerenciamento de Imagens: Implementação de captura de câmera nativa.

## ✨ Funcionalidades Principais
- Fluxo de Quiz Dinâmico: Telas específicas para cada tipo de pergunta (Pele, Rotina, Preocupação).
- Integração com Câmera: Tela dedicada para captura segura da foto facial.
- Consumo de API: Comunicação assíncrona para envio de dados e recebimento de recomendações.
- Resultados Customizados: Exibição de produtos Dermage baseados na análise da IA.

## 📂 Estrutura de Arquivos (Destaques)
- CapturaImagemActivity.kt: Gerencia a abertura da câmera e o armazenamento temporário da imagem.
- ApiService.kt & RetrofitClient.kt: Configuração da comunicação com o servidor/IA.
- Quest...Activity.kt: Conjunto de atividades que compõem o questionário de diagnóstico.
- ResultadoActivity.kt: Processa e exibe o feedback da IA e a vitrine de produtos.

## 🚀 Como Rodar Localmente
Para testar este projeto em seu ambiente de desenvolvimento:

1. Pré-requisitos:

- Android Studio instalado (versão Ladybug ou superior recomendada).
- SDK do Android configurada (API 21 ou superior).

2. Clone o Repositório:
- git clone https://github.com/seu-usuario/nome-do-repositorio.git

3. Importe no Android Studio:
- Abra o Android Studio.
- Selecione Open... e escolha a pasta do projeto.

4. Configuração da API:
- Verifique o arquivo RetrofitClient.kt e certifique-se de que a BASE_URL da API está acessível ou configurada corretamente para o seu ambiente de teste.

5. Execução:
- Conecte um dispositivo físico (recomendado para testar a câmera) ou use um Emulador com suporte a câmera.
- Clique em Run (ícone de play verde).

## 🎓 Contexto Acadêmico
Este projeto foi desenvolvido como parte de uma parceria entre o Ibmec e a Dermage, visando aplicar tecnologia de ponta para melhorar a experiência de consumo no mercado de dermocosméticos.

#### Desenvolvido por [LucasKronemberger] 📱✨

## 🤖 Dermage AI Skin Analysis - Backend
Este é o motor de inteligência artificial por trás do aplicativo Dermage. Trata-se de uma API de alto desempenho desenvolvida com FastAPI e Pydantic AI, que utiliza o modelo Gemini 2.5 Pro para realizar diagnósticos dermatológicos preliminares e recomendações de produtos.

## 🧠 Inteligência e Lógica
O backend utiliza um Agente de IA Dermatologista configurado para:
- Análise Multimodal: Processa simultaneamente os dados textuais do quiz (perfil do usuário) e as imagens capturadas pela câmera.
- Prompt de Especialista: O agente atua como um dermatologista experiente, utilizando linguagem técnica e empática.
- Curadoria Dermage: Todas as recomendações de produtos são estritamente vinculadas ao catálogo oficial da Dermage.

## 🛠️ Tecnologias Utilizadas
- Framework: FastAPI (Assíncrono e tipado).
- IA Engine: Pydantic AI para orquestração de agentes.
- LLM: gemini-2.5-pro (Google).
- Validação de Dados: Pydantic v2.
- Containerização: Docker.

## 🚀 Como Rodar Localmente
1. Pré-requisitos
- Python 3.12+
- Uma chave de API do Google AI Studio (Gemini API) ou Google Cloud Vertex AI.

2. Instalação Manual
- Clone o repositório
git clone https://github.com/jpgiovanelli/ibmec.projeto-mobile.backend.git
cd ibmec.projeto-mobile.backend

- Crie um ambiente virtual
python -m venv venv
venv\Scripts\activate

- Instale as dependências
pip install -r requirements.txt

3. Variáveis de Ambiente
Crie um arquivo .env na raiz do projeto (ou exporte no seu terminal):
- GOOGLE_API_KEY=sua_chave_aqui

4. Execução
- uvicorn app.main:app --reload
A API estará disponível em http://localhost:8000. Acesse /docs para visualizar a documentação interativa (Swagger).

## 🐳 Rodando com Docker
Se preferir um ambiente isolado, utilize o Dockerfile fornecido:
- Build da imagem
docker build -t dermage-api .

- Execução do container
docker run -p 8000:8000 --env GOOGLE_API_KEY=sua_chave_aqui dermage-api

## 📌 Endpoints Principais
### POST /analyze
Endpoint principal que recebe o formulário do quiz e a foto.
- Parâmetros (Multipart/Form-Data):
 - skinData: String JSON contendo o perfil do usuário (Idade, tipo de pele, etc).
 - images: Arquivo(s) de imagem capturados pelo app Android.
- Resposta: Um objeto AnalysisResponse contendo o diagnóstico, recomendações de rotina e links para produtos Dermage.

📂 Estrutura do Backend
- app/main.py: Ponto de entrada da API, gerenciamento de CORS e handlers de erro.
- app/ai/AiServices.py: Configuração do agente Pydantic AI e lógica de interação com o Gemini.
- app/models/: Definições de schemas Pydantic para entrada e saída de dados.

Projeto desenvolvido para a parceria Dermage & Ibmec. 🚀

#### Responsável pela API: https://github.com/jpgiovanelli/ibmec.projeto-mobile.backend
