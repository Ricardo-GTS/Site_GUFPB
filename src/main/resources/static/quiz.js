import { questions } from './Perguntas/Perguntas_Inicial.js';
import { questionsAgrariasAmbientais } from './Perguntas/AgrariasAmbientais.js';
import {questionsBiologicasSaude } from './Perguntas/BiologiaSaude.js';
import {questionsExatasTecnologia} from './Perguntas/ExatasTecnologia.js';
import {questionsHumanasSociais} from './Perguntas/HumanasSociais.js';

function generateQuiz(questionsToGenerate) {
    const questionsElement = document.getElementById("questions");
    questionsElement.innerHTML = ""; // Limpa as perguntas anteriores caso a função seja chamada novamente

    questionsToGenerate.forEach((q, qIndex) => {
        const questionDiv = document.createElement("div");
        const questionText = document.createElement("p");
        questionText.textContent = (qIndex + 1) + ". " + q.question;
        questionDiv.appendChild(questionText);

        q.options.forEach((option, oIndex) => {
            const label = document.createElement("label");
            const input = document.createElement("input");
            input.type = "radio";
            input.name = `question${qIndex}`;
            input.value = JSON.stringify(option.score);
            label.appendChild(input);
            label.appendChild(document.createTextNode(option.text));
            questionDiv.appendChild(label);
            questionDiv.appendChild(document.createElement("br"));
        });

        questionsElement.appendChild(questionDiv);
    });
}


function calculateScores() {
    const scores = {};
    const formData = new FormData(document.getElementById("quizForm"));
    for (let [key, value] of formData.entries()) {
        const questionScore = JSON.parse(value);
        for (let area in questionScore) {
            if (!scores[area]) scores[area] = 0;
            scores[area] += questionScore[area];
        }
    }
    return scores;
}

document.getElementById("quizForm").addEventListener("submit", function(e) {
    e.preventDefault();
    const scores = calculateScores();
    const areaEscolhida = Object.keys(scores).reduce((a, b) => scores[a] > scores[b] ? a : b);

    if (areaEscolhida === "Ciências Exatas e Tecnologia") {
        generateQuiz(questionsExatasTecnologia);
    } 
    else if (areaEscolhida === "Ciências Humanas e Sociais") {
        generateQuiz(questionsHumanasSociais);
    }
    else if (areaEscolhida === "Ciências Agrárias e Ambientais") {
        generateQuiz(questionsAgrariasAmbientais);
    } 
    else if (areaEscolhida === "Ciências Biológicas e da Saúde") {
        generateQuiz(questionsBiologicasSaude);
    } 
    else {
        let token = localStorage.getItem('authToken'); // Recupera o token do localStorage
        fetch('/questionario/resposta', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({areaEscolhida, scores}),
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok: ' + response.statusText);
            }
            return response.json().catch(() => ({
                message: "Received non-JSON response",
                data: response.statusText
            }));
        })
        .then(data => {
            alert("Resposta enviada com sucesso!");
            console.log(data);
            window.location.href = '/recomendar';
        })
        .catch((error) => {
            console.error('Erro:', error);
            alert('Erro ao enviar a resposta: ' + error.message);
        });
    }
});

generateQuiz(questions); // Inicializa com o primeiro questionário

