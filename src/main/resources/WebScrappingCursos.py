import requests
from bs4 import BeautifulSoup
import csv

def extrair_dados_tabela(url):
    """ Extrai dados de uma tabela específica de uma URL fornecida. """
    response = requests.get(url)
    response.raise_for_status()
    soup = BeautifulSoup(response.text, 'html.parser')
    tabela = soup.find('table', class_='formulario', width='700px')
    if not tabela:
        return []

    dados_tabela = []
    rows = tabela.find_all('tr')
    for row in rows:
        cols = row.find_all('th') + row.find_all('td')
        # Combina o texto de todas as células, substituindo vírgulas por espaços
        col_text = ' '.join(ele.text.strip().replace(',', ' ').replace('\n',' ') for ele in cols)
        if len(col_text) < 200:  # Verifica o comprimento do texto combinado
            dados_tabela.append(col_text)
    return dados_tabela

# URL da página
url = "https://sigaa.ufpb.br/sigaa/public/curso/lista.jsf?nivel=G&aba=p-graduacao"
response = requests.get(url)
response.raise_for_status()
soup = BeautifulSoup(response.text, 'html.parser')
resultados = []

for sub_form in soup.find_all('td', class_='subFormulario'):
    centro = sub_form.text.strip()
    centro = centro.replace(',', ' ')
    centro = centro.replace('.', ' ')
    sigla = centro.split(" - ")[0]
    cursos = []
    next_tr = sub_form.parent.find_next_sibling('tr')

    while next_tr and not next_tr.find('td', class_='subFormulario'):
        if 'linhaPar' in next_tr['class'] or 'linhaImpar' in next_tr['class']:
            curso = next_tr.find('td').text.strip()
            curso = curso.replace(',', ' ')
            link = next_tr.find('a')['href']
            link = link.split('=')[1]
            full_link = "https://sigaa.ufpb.br/sigaa/public/curso/curriculo.jsf?lc=pt_BR&id=" + link
            cursos.append({'sigla': sigla,'curso': curso, 'full_link': full_link})
        next_tr = next_tr.find_next_sibling('tr')
    
    resultados.append({'Centro': centro, 'Cursos': cursos})

# Preparando para escrever no arquivo CSV
with open('cursos_info.csv', 'w', newline='', encoding='utf-8') as file:
    writer = csv.writer(file)
    writer.writerow(['Curso', 'Centro', 'Sigla', 'Dados da Tabela'])

    for resultado in resultados:
        for curso in resultado['Cursos']:
            res = requests.get(curso['full_link'])
            res.raise_for_status()
            soup_link = BeautifulSoup(res.text, 'html.parser')
            a_tags = soup_link.find_all('a', onclick=True)
            for tag in a_tags:
                onclick_text = tag['onclick']
                if "jsfcljs" in onclick_text:
                    start = onclick_text.find("'id':") + 6
                    end = onclick_text.find("'}", start)
                    id_curso = onclick_text[start:end]
                    link_grade = "https://sigaa.ufpb.br/sigaa/link/public/curso/curriculo/" + id_curso
                    curso['link_grade'] = link_grade
                    curso['dados_tabela'] = extrair_dados_tabela(link_grade)
                    dados_tabela = ' | '.join(curso['dados_tabela'])
                    writer.writerow([curso['curso'], resultado['Centro'], curso['sigla'], dados_tabela])
                    break

print("Dados exportados para 'cursos_info.csv'")
