// ===================== CONFIGURACIÓN =====================
const API_BASE_URL = '/api';
const API_GAMES = `${API_BASE_URL}/games`;
const API_AUTH = `${API_BASE_URL}/auth`;

// ===================== ESTADO DE LA APLICACIÓN =====================
let appState = {
    games: [],
    currentGame: null,
    editingGameId: null,
    users: []
};

// ===================== ELEMENTOS DEL DOM =====================
const elements = {
    // Botones
    btnCreateGame: document.getElementById('btnCreateGame'),
    btnSearch: document.getElementById('btnSearch'),
    btnCloseGameModal: document.getElementById('btnCloseGameModal'),
    btnCancelGame: document.getElementById('btnCancelGame'),
    btnCloseDetailsModal: document.getElementById('btnCloseDetailsModal'),
    btnEditGame: document.getElementById('btnEditGame'),
    btnDeleteGame: document.getElementById('btnDeleteGame'),
    btnCloseDetails: document.getElementById('btnCloseDetails'),
    btnCloseSectionsModal: document.getElementById('btnCloseSectionsModal'),
    btnCancelSection: document.getElementById('btnCancelSection'),
    
    // Modales
    gameModal: document.getElementById('gameModal'),
    detailsModal: document.getElementById('detailsModal'),
    sectionsModal: document.getElementById('sectionsModal'),
    
    // Formularios
    gameForm: document.getElementById('gameForm'),
    sectionForm: document.getElementById('sectionForm'),
    
    // Inputs
    searchInput: document.getElementById('searchInput'),
    gameName: document.getElementById('gameName'),
    gameGenre: document.getElementById('gameGenre'),
    gameDescription: document.getElementById('gameDescription'),
    gameCategories: document.getElementById('gameCategories'),
    gameTags: document.getElementById('gameTags'),
    sectionTitle: document.getElementById('sectionTitle'),
    sectionType: document.getElementById('sectionType'),
    sectionContent: document.getElementById('sectionContent'),
    
    // Contenedores
    gamesGrid: document.getElementById('gamesGrid'),
    toast: document.getElementById('toast'),
    modalTitle: document.getElementById('modalTitle'),
    detailsTitle: document.getElementById('detailsTitle'),
    detailsBody: document.getElementById('detailsBody'),
    
    // Stats
    totalGames: document.getElementById('totalGames'),
    totalGenres: document.getElementById('totalGenres'),
    totalCategories: document.getElementById('totalCategories')
};

// ===================== INICIALIZACIÓN =====================
document.addEventListener('DOMContentLoaded', () => {
    console.log('Game Concept Hub cargado');
    loadGames();
    setupEventListeners();
});

// ===================== EVENT LISTENERS =====================
function setupEventListeners() {
    // Crear juego
    elements.btnCreateGame.addEventListener('click', openCreateGameModal);
    elements.gameForm.addEventListener('submit', handleGameFormSubmit);
    elements.btnCloseGameModal.addEventListener('click', closeGameModal);
    elements.btnCancelGame.addEventListener('click', closeGameModal);
    
    // Búsqueda
    elements.btnSearch.addEventListener('click', handleSearch);
    elements.searchInput.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') handleSearch();
    });
    
    // Detalles
    elements.btnCloseDetailsModal.addEventListener('click', closeDetailsModal);
    elements.btnCloseDetails.addEventListener('click', closeDetailsModal);
    elements.btnEditGame.addEventListener('click', editCurrentGame);
    elements.btnDeleteGame.addEventListener('click', deleteCurrentGame);
    
    // Secciones
    elements.sectionForm.addEventListener('submit', handleSectionFormSubmit);
    elements.btnCloseSectionsModal.addEventListener('click', closeSectionsModal);
    elements.btnCancelSection.addEventListener('click', closeSectionsModal);
    
    // Cerrar modales con ESC
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') {
            closeGameModal();
            closeDetailsModal();
            closeSectionsModal();
        }
    });
}

// ===================== CARGAR JUEGOS =====================
async function loadGames() {
    try {
        showToast('Cargando juegos...', 'info');
        const response = await fetch(`${API_GAMES}/search?query=`);
        
        if (!response.ok) {
            throw new Error('Error al cargar juegos');
        }
        
        const games = await response.json();
        appState.games = games || [];
        renderGames(games);
        updateStats();
        showToast('Juegos cargados correctamente', 'success');
    } catch (error) {
        console.error('Error:', error);
        showToast('Error al cargar los juegos', 'error');
        renderEmptyState();
    }
}

// ===================== BÚSQUEDA =====================
async function handleSearch() {
    const query = elements.searchInput.value.trim();
    
    if (!query) {
        loadGames();
        return;
    }
    
    try {
        showToast('Buscando juegos...', 'info');
        const response = await fetch(`${API_GAMES}/search?query=${encodeURIComponent(query)}`);
        
        if (!response.ok) {
            throw new Error('Error en la búsqueda');
        }
        
        const games = await response.json();
        renderGames(games);
        showToast(`Se encontraron ${games.length} resultados`, 'success');
    } catch (error) {
        console.error('Error:', error);
        showToast('Error en la búsqueda', 'error');
    }
}

// ===================== RENDERIZAR JUEGOS =====================
function renderGames(games) {
    if (!games || games.length === 0) {
        renderEmptyState();
        return;
    }
    
    elements.gamesGrid.innerHTML = games.map(game => createGameCard(game)).join('');
    
    // Añadir event listeners a las tarjetas
    document.querySelectorAll('.game-card').forEach((card, index) => {
        card.addEventListener('click', () => showGameDetails(games[index]));
    });
    
    document.querySelectorAll('.btn-edit-card').forEach((btn, index) => {
        btn.addEventListener('click', (e) => {
            e.stopPropagation();
            appState.currentGame = games[index];
            openEditGameModal(games[index]);
        });
    });
    
    document.querySelectorAll('.btn-delete-card').forEach((btn, index) => {
        btn.addEventListener('click', (e) => {
            e.stopPropagation();
            if (confirm('¿Estás seguro de que deseas eliminar este juego?')) {
                deleteGame(games[index].id);
            }
        });
    });
    
    document.querySelectorAll('.btn-add-section').forEach((btn, index) => {
        btn.addEventListener('click', (e) => {
            e.stopPropagation();
            appState.currentGame = games[index];
            openSectionsModal();
        });
    });
}

function createGameCard(game) {
    const description = game.description ? game.description.substring(0, 100) + '...' : 'Sin descripción';
    
    return `
        <div class="game-card">
            <div class="game-card-header">
                <h3 class="game-card-title">${escapeHtml(game.name)}</h3>
                <span class="game-card-genre">${escapeHtml(game.genre || 'Sin género')}</span>
            </div>
            <div class="game-card-body">
                <p class="game-description">${escapeHtml(description)}</p>
                <div class="game-meta">
                    ${game.categories && game.categories.length > 0 ? `
                        <div class="game-meta-item">
                            <span>📁 ${game.categories.length} categoría(s)</span>
                        </div>
                    ` : ''}
                    ${game.tags && game.tags.length > 0 ? `
                        <div class="game-meta-item">
                            <span>🏷️ ${game.tags.length} tag(s)</span>
                        </div>
                    ` : ''}
                </div>
            </div>
            <div class="game-card-footer">
                <button class="btn btn-primary btn-edit-card" title="Editar">✏️ Editar</button>
                <button class="btn btn-primary btn-add-section" title="Añadir sección">📝 Sección</button>
                <button class="btn btn-danger btn-delete-card" title="Eliminar">🗑️ Eliminar</button>
            </div>
        </div>
    `;
}

function renderEmptyState() {
    elements.gamesGrid.innerHTML = `
        <div class="empty-state">
            <p>📭 No hay juegos registrados aún</p>
            <p class="empty-subtitle">¡Crea tu primer juego para comenzar!</p>
        </div>
    `;
}

// ===================== ACTUALIZAR ESTADÍSTICAS =====================
function updateStats() {
    elements.totalGames.textContent = appState.games.length;
    
    const genres = new Set(appState.games.map(g => g.genre).filter(Boolean));
    elements.totalGenres.textContent = genres.size;
    
    const categories = new Set();
    appState.games.forEach(game => {
        if (game.categories) {
            game.categories.forEach(cat => categories.add(cat.name));
        }
    });
    elements.totalCategories.textContent = categories.size;
}

// ===================== MODAL: CREAR JUEGO =====================
function openCreateGameModal() {
    appState.editingGameId = null;
    elements.modalTitle.textContent = 'Nuevo Juego';
    elements.gameForm.reset();
    openModal(elements.gameModal);
}

function openEditGameModal(game) {
    appState.editingGameId = game.id;
    elements.modalTitle.textContent = `Editar: ${game.name}`;
    
    elements.gameName.value = game.name;
    elements.gameGenre.value = game.genre || '';
    elements.gameDescription.value = game.description || '';
    elements.gameCategories.value = game.categories ? game.categories.map(c => c.name).join(', ') : '';
    elements.gameTags.value = game.tags ? game.tags.map(t => t.name).join(', ') : '';
    
    openModal(elements.gameModal);
}

function closeGameModal() {
    closeModal(elements.gameModal);
    elements.gameForm.reset();
    appState.editingGameId = null;
}

async function handleGameFormSubmit(e) {
    e.preventDefault();
    
    const gameData = {
        name: elements.gameName.value,
        genre: elements.gameGenre.value,
        description: elements.gameDescription.value,
        categories: parseArrayInput(elements.gameCategories.value),
        tags: parseArrayInput(elements.gameTags.value),
        sections: appState.currentGame?.sections || []
    };
    
    try {
        const endpoint = appState.editingGameId 
            ? `${API_GAMES}/${appState.editingGameId}`
            : API_GAMES;
        
        const method = appState.editingGameId ? 'PUT' : 'POST';
        
        const response = await fetch(endpoint, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(gameData)
        });
        
        if (!response.ok) {
            throw new Error(response.status === 404 ? 'Juego no encontrado' : 'Error al guardar');
        }
        
        const savedGame = await response.json();
        
        closeGameModal();
        showToast(
            appState.editingGameId ? 'Juego actualizado correctamente' : 'Juego creado correctamente',
            'success'
        );
        
        loadGames();
    } catch (error) {
        console.error('Error:', error);
        showToast('Error al guardar el juego', 'error');
    }
}

// ===================== MODAL: DETALLES =====================
function showGameDetails(game) {
    appState.currentGame = game;
    elements.detailsTitle.textContent = game.name;
    
    const detailsHTML = `
        <div class="details-content">
            <div class="detail-section">
                <h3>Información General</h3>
                <p><strong>Nombre:</strong> ${escapeHtml(game.name)}</p>
                <p><strong>Género:</strong> ${escapeHtml(game.genre || 'No especificado')}</p>
                <p><strong>Descripción:</strong> ${escapeHtml(game.description || 'Sin descripción')}</p>
            </div>
            
            ${game.categories && game.categories.length > 0 ? `
                <div class="detail-section">
                    <h3>Categorías</h3>
                    <div class="tags-list">
                        ${game.categories.map(cat => `<span class="tag">${escapeHtml(cat.name)}</span>`).join('')}
                    </div>
                </div>
            ` : ''}
            
            ${game.tags && game.tags.length > 0 ? `
                <div class="detail-section">
                    <h3>Tags</h3>
                    <div class="tags-list">
                        ${game.tags.map(tag => `<span class="tag">${escapeHtml(tag.name)}</span>`).join('')}
                    </div>
                </div>
            ` : ''}
            
            ${game.sections && game.sections.length > 0 ? `
                <div class="detail-section">
                    <h3>Secciones</h3>
                    ${game.sections.map(section => `
                        <div style="margin-bottom: 15px; padding: 12px; background: white; border-radius: 6px;">
                            <strong>${escapeHtml(section.title)}</strong> <em>(${section.type})</em>
                            <p style="margin-top: 8px; color: #6b7280;">${escapeHtml(section.content)}</p>
                        </div>
                    `).join('')}
                </div>
            ` : ''}
        </div>
    `;
    
    elements.detailsBody.innerHTML = detailsHTML;
    openModal(elements.detailsModal);
}

function closeDetailsModal() {
    closeModal(elements.detailsModal);
    appState.currentGame = null;
}

function editCurrentGame() {
    if (appState.currentGame) {
        closeDetailsModal();
        openEditGameModal(appState.currentGame);
    }
}

async function deleteCurrentGame() {
    if (!appState.currentGame) return;
    
    if (confirm(`¿Estás seguro de que deseas eliminar "${appState.currentGame.name}"?`)) {
        await deleteGame(appState.currentGame.id);
        closeDetailsModal();
    }
}

async function deleteGame(gameId) {
    try {
        const response = await fetch(`${API_GAMES}/${gameId}`, {
            method: 'DELETE'
        });
        
        if (!response.ok) {
            throw new Error('Error al eliminar');
        }
        
        showToast('Juego eliminado correctamente', 'success');
        loadGames();
    } catch (error) {
        console.error('Error:', error);
        showToast('Error al eliminar el juego', 'error');
    }
}

// ===================== MODAL: SECCIONES =====================
function openSectionsModal() {
    if (!appState.currentGame) return;
    elements.sectionForm.reset();
    openModal(elements.sectionsModal);
}

function closeSectionsModal() {
    closeModal(elements.sectionsModal);
    elements.sectionForm.reset();
}

async function handleSectionFormSubmit(e) {
    e.preventDefault();
    
    if (!appState.currentGame) return;
    
    const section = {
        title: elements.sectionTitle.value,
        type: elements.sectionType.value,
        content: elements.sectionContent.value
    };
    
    if (!appState.currentGame.sections) {
        appState.currentGame.sections = [];
    }
    
    appState.currentGame.sections.push(section);
    
    try {
        const response = await fetch(`${API_GAMES}/${appState.currentGame.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(appState.currentGame)
        });
        
        if (!response.ok) {
            throw new Error('Error al guardar la sección');
        }
        
        closeSectionsModal();
        showToast('Sección añadida correctamente', 'success');
        loadGames();
    } catch (error) {
        console.error('Error:', error);
        showToast('Error al añadir la sección', 'error');
    }
}

// ===================== UTILIDADES =====================
function openModal(modal) {
    modal.classList.add('active');
    document.body.style.overflow = 'hidden';
}

function closeModal(modal) {
    modal.classList.remove('active');
    document.body.style.overflow = 'auto';
}

function showToast(message, type = 'success') {
    elements.toast.textContent = message;
    elements.toast.className = `toast show ${type}`;
    
    setTimeout(() => {
        elements.toast.classList.remove('show');
    }, 3000);
}

function parseArrayInput(input) {
    return input
        .split(',')
        .map(item => ({
            name: item.trim()
        }))
        .filter(item => item.name.length > 0);
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// ===================== SINCRONIZACIÓN =====================
// Cargar juegos cada 30 segundos
setInterval(() => {
    if (!elements.gameModal.classList.contains('active')) {
        loadGames();
    }
}, 30000);
