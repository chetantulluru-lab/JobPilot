"""add_roadmaps_and_mvp_schema

Revision ID: f31c8de12a4b
Revises: ead46dfb13be
Create Date: 2026-09-14 17:15:00.000000

"""
from typing import Sequence, Union
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision: str = 'f31c8de12a4b'
down_revision: Union[str, Sequence[str], None] = 'ead46dfb13be'
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None


def upgrade() -> None:
    # 1. Create roadmaps table
    op.create_table(
        'roadmaps',
        sa.Column('id', sa.String(length=36), nullable=False),
        sa.Column('user_id', sa.String(length=36), nullable=False),
        sa.Column('title', sa.String(length=255), nullable=False),
        sa.Column('goal', sa.String(length=255), nullable=False),
        sa.Column('duration', sa.String(length=50), nullable=False),
        sa.Column('total_days', sa.Integer(), nullable=False),
        sa.Column('completed_days', sa.Integer(), nullable=False),
        sa.Column('progress_percentage', sa.Integer(), nullable=False),
        sa.Column('is_completed', sa.Boolean(), nullable=False),
        sa.Column('skills_learned_json', sa.Text(), nullable=False),
        sa.Column('created_at', sa.DateTime(timezone=True), nullable=False),
        sa.Column('updated_at', sa.DateTime(timezone=True), nullable=False),
        sa.ForeignKeyConstraint(['user_id'], ['users.id'], ondelete='CASCADE'),
        sa.PrimaryKeyConstraint('id')
    )
    op.create_index(op.f('ix_roadmaps_id'), 'roadmaps', ['id'], unique=False)
    op.create_index(op.f('ix_roadmaps_user_id'), 'roadmaps', ['user_id'], unique=False)

    # 2. Create roadmap_phases table
    op.create_table(
        'roadmap_phases',
        sa.Column('id', sa.String(length=36), nullable=False),
        sa.Column('roadmap_id', sa.String(length=36), nullable=False),
        sa.Column('phase_number', sa.Integer(), nullable=False),
        sa.Column('title', sa.String(length=255), nullable=False),
        sa.Column('description', sa.Text(), nullable=True),
        sa.Column('is_unlocked', sa.Boolean(), nullable=False),
        sa.Column('is_completed', sa.Boolean(), nullable=False),
        sa.Column('project_title', sa.String(length=255), nullable=True),
        sa.Column('project_description', sa.Text(), nullable=True),
        sa.Column('created_at', sa.DateTime(timezone=True), nullable=False),
        sa.ForeignKeyConstraint(['roadmap_id'], ['roadmaps.id'], ondelete='CASCADE'),
        sa.PrimaryKeyConstraint('id')
    )
    op.create_index(op.f('ix_roadmap_phases_id'), 'roadmap_phases', ['id'], unique=False)
    op.create_index(op.f('ix_roadmap_phases_roadmap_id'), 'roadmap_phases', ['roadmap_id'], unique=False)

    # 3. Create roadmap_days table
    op.create_table(
        'roadmap_days',
        sa.Column('id', sa.String(length=36), nullable=False),
        sa.Column('roadmap_id', sa.String(length=36), nullable=False),
        sa.Column('phase_id', sa.String(length=36), nullable=False),
        sa.Column('day_number', sa.Integer(), nullable=False),
        sa.Column('topic', sa.String(length=255), nullable=False),
        sa.Column('learning_objective', sa.Text(), nullable=True),
        sa.Column('subtopics_json', sa.Text(), nullable=False),
        sa.Column('practice_tasks_json', sa.Text(), nullable=False),
        sa.Column('is_completed', sa.Boolean(), nullable=False),
        sa.Column('completed_at', sa.DateTime(timezone=True), nullable=True),
        sa.ForeignKeyConstraint(['phase_id'], ['roadmap_phases.id'], ondelete='CASCADE'),
        sa.ForeignKeyConstraint(['roadmap_id'], ['roadmaps.id'], ondelete='CASCADE'),
        sa.PrimaryKeyConstraint('id')
    )
    op.create_index(op.f('ix_roadmap_days_id'), 'roadmap_days', ['id'], unique=False)
    op.create_index(op.f('ix_roadmap_days_phase_id'), 'roadmap_days', ['phase_id'], unique=False)
    op.create_index(op.f('ix_roadmap_days_roadmap_id'), 'roadmap_days', ['roadmap_id'], unique=False)

    # 4. Create roadmap_resources table
    op.create_table(
        'roadmap_resources',
        sa.Column('id', sa.String(length=36), nullable=False),
        sa.Column('day_id', sa.String(length=36), nullable=True),
        sa.Column('phase_id', sa.String(length=36), nullable=False),
        sa.Column('title', sa.String(length=255), nullable=False),
        sa.Column('url', sa.String(length=1000), nullable=False),
        sa.Column('language', sa.String(length=50), nullable=False),
        sa.Column('resource_type', sa.String(length=50), nullable=False),
        sa.Column('source', sa.String(length=100), nullable=False),
        sa.ForeignKeyConstraint(['day_id'], ['roadmap_days.id'], ondelete='CASCADE'),
        sa.ForeignKeyConstraint(['phase_id'], ['roadmap_phases.id'], ondelete='CASCADE'),
        sa.PrimaryKeyConstraint('id')
    )
    op.create_index(op.f('ix_roadmap_resources_id'), 'roadmap_resources', ['id'], unique=False)
    op.create_index(op.f('ix_roadmap_resources_day_id'), 'roadmap_resources', ['day_id'], unique=False)
    op.create_index(op.f('ix_roadmap_resources_phase_id'), 'roadmap_resources', ['phase_id'], unique=False)

    # 5. Create learning_activities table
    op.create_table(
        'learning_activities',
        sa.Column('id', sa.String(length=36), nullable=False),
        sa.Column('user_id', sa.String(length=36), nullable=False),
        sa.Column('activity_type', sa.String(length=50), nullable=False),
        sa.Column('reference_id', sa.String(length=36), nullable=True),
        sa.Column('activity_date', sa.Date(), nullable=False),
        sa.Column('created_at', sa.DateTime(timezone=True), nullable=False),
        sa.ForeignKeyConstraint(['user_id'], ['users.id'], ondelete='CASCADE'),
        sa.PrimaryKeyConstraint('id')
    )
    op.create_index(op.f('ix_learning_activities_id'), 'learning_activities', ['id'], unique=False)
    op.create_index(op.f('ix_learning_activities_user_id'), 'learning_activities', ['user_id'], unique=False)

    # 6. Add columns to career_profiles
    op.add_column('career_profiles', sa.Column('current_streak', sa.Integer(), nullable=False, server_default='0'))
    op.add_column('career_profiles', sa.Column('longest_streak', sa.Integer(), nullable=False, server_default='0'))
    op.add_column('career_profiles', sa.Column('last_activity_date', sa.Date(), nullable=True))

    # 7. Add columns to personal_infos
    op.add_column('personal_infos', sa.Column('age', sa.Integer(), nullable=True))
    op.add_column('personal_infos', sa.Column('college', sa.String(length=255), nullable=True))
    op.add_column('personal_infos', sa.Column('degree', sa.String(length=100), nullable=True))
    op.add_column('personal_infos', sa.Column('branch', sa.String(length=100), nullable=True))

    # 8. Add analysis_json to resumes
    op.add_column('resumes', sa.Column('analysis_json', sa.Text(), nullable=True))


def downgrade() -> None:
    op.drop_column('resumes', 'analysis_json')
    op.drop_column('personal_infos', 'branch')
    op.drop_column('personal_infos', 'degree')
    op.drop_column('personal_infos', 'college')
    op.drop_column('personal_infos', 'age')
    op.drop_column('career_profiles', 'last_activity_date')
    op.drop_column('career_profiles', 'longest_streak')
    op.drop_column('career_profiles', 'current_streak')

    op.drop_table('learning_activities')
    op.drop_table('roadmap_resources')
    op.drop_table('roadmap_days')
    op.drop_table('roadmap_phases')
    op.drop_table('roadmaps')
