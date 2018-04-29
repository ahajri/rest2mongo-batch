import * as moment from "moment";
import { CalendarEvent, RecurringPeriod } from "./calendar-models";

export class Block
{
	public rect: Rect;
	public isStart: boolean;
	public isEnd: boolean;
	public event: CalendarEvent;
}

export class Rect
{
	public x: number;
	public y: number;
	public width: number;
	public height: number;
}

export class CalendarService
{
	public getDayViewBlocks(rcCalendar: Rect | null, events: Array<CalendarEvent>, start: moment.Moment, days: number, cellHeight: number): Array<Block>
	{
		if (!rcCalendar)
			return [];

		let end = moment(start).add(days, "days");

		let projectedEvents = this.getProjectedEvents(events, end);

		let slicedEvents = this.sliceDayEvents(projectedEvents);

		let startOfDay = moment(start).startOf("day");
		let endOfDay = moment(start).endOf("day");

		let blocks: Array<Block> = [];
		let dayBlocks: Array<Block> = [];

		for (let i = 0; i < days; i++)
		{
			for (let j = 0; j < slicedEvents.length; j++)
			{
				let event = slicedEvents[j];
				if (event.start.isBetween(startOfDay, endOfDay, undefined, "[]"))
				{
					let block = this.createBlock(event, rcCalendar, days, 60, i, cellHeight);
					if (!!block)
						dayBlocks.push(block);
				}
			}

			this.adjustBlocksWidth(dayBlocks);
			blocks = blocks.concat(dayBlocks);

			dayBlocks = [];

			startOfDay = moment(startOfDay).add(1, "days");
			endOfDay = moment(startOfDay).endOf("day");
		}

		return blocks;
	}

	private createBlock(event: CalendarEvent, rcCalendar: Rect, days: number, timeColumnWidth: number, daysOffset: number, cellHeight: number): Block | null
	{
		let blockLeft = rcCalendar.x + timeColumnWidth;
		let blockWidth = (rcCalendar.width - timeColumnWidth) / days;

		let calendarY = rcCalendar.y + (cellHeight * 3);

		///////////////////////////////////////////////////////////////////////////////

		let block = new Block();
		block.isStart = event.isStartInternal;
		block.isEnd = event.isEndInternal;
		block.event = event.clone();

		let minutes = moment(event.end).diff(event.start, "minutes");
		if (minutes <= 0)
			return null; // TODO

		let blockHeight = (minutes / 60) * cellHeight;

		minutes = moment(event.start).diff(moment(event.start).startOf("day"), "minutes");
		let blockTop = (minutes / 60) * cellHeight;
		blockTop += calendarY + 2;

		if (blockHeight <= 0)
			return null;

		block.rect = {
			x: blockLeft + (daysOffset * blockWidth),
			y: blockTop,
			width: blockWidth,
			height: blockHeight
		};

		return block;
	}

	public getProjectedEvents(events: Array<CalendarEvent>, end: moment.Moment): Array<CalendarEvent>
	{
		let actualEvents: Array<CalendarEvent> = [];

		for (let i = 0; i < events.length; i++)
		{
			let event = events[i];

			actualEvents.push(event);

			switch (event.recurring)
			{
				case RecurringPeriod.Day:
					this.insertRecurringEvents(event, actualEvents, end, "days");
					break;

				case RecurringPeriod.Week:
					this.insertRecurringEvents(event, actualEvents, end, "weeks");
					break;

				case RecurringPeriod.Month:
					this.insertRecurringEvents(event, actualEvents, end, "months");
					break;

				case RecurringPeriod.None:
				default:
					break;
			}
		}

		return actualEvents;
	}

	private sliceDayEvents(events: Array<CalendarEvent>): Array<CalendarEvent>
	{
		let slicedEvents: Array<CalendarEvent> = [];

		for (let i = 0; i < events.length; i++)
		{
			let event = events[i];
			if (event.allDay)
				continue;

			if (event.start.year() !== event.end.year() || event.start.month() !== event.end.month() || event.start.date() !== event.end.date())
			{
				let endOfDay = moment(event.start).endOf("day");
				let firstSlice = event.clone();
				firstSlice.end = endOfDay;
				firstSlice.isStartInternal = true;

				slicedEvents.push(firstSlice);

				///////////////////////////////////////////////////////////////////////////////

				let startOfNextDay = moment(event.start).startOf("day").add(1, "day");
				let minutes = moment(event.end).diff(startOfNextDay, "minutes");
				while (minutes > 0)
				{
					let nextSlice = event.clone();
					nextSlice.start = moment(startOfNextDay);
					if (minutes < 24 * 60)
						nextSlice.isEndInternal = true;

					minutes = Math.min(minutes, (24 * 60) - 1);
					nextSlice.end = moment(startOfNextDay).add(minutes, "minutes");

					slicedEvents.push(nextSlice);

					///////////////////////////////////////////////////////////////////////////////

					startOfNextDay = moment(startOfNextDay).add(1, "day");
					minutes = moment(event.end).diff(startOfNextDay, "minutes");
				}
			}
			else
			{
				event.isStartInternal = true;
				event.isEndInternal = true;

				slicedEvents.push(event);
			}
		}

		return slicedEvents;
	}

	private adjustBlocksWidth(blocks: Array<Block>)
	{
		if (blocks.length > 1)
		{
			let offset = 0;
			for (let i = 0; i < blocks.length; i++)
			{
				let block = blocks[i];
				let overlappedCount = this.getOverlappedBlocksCount(i, block.rect, blocks);
				if (overlappedCount > 0)
				{
					block.rect.x += offset;
					block.rect.width = block.rect.width / (overlappedCount + 1);
					offset += block.rect.width;
				}
				else
					offset = 0;
			}
		}
	}

	private getOverlappedBlocksCount(index: number, rc: Rect, blocks: Array<Block>): number
	{
		let count = 0;

		let blockTop = rc.y;
		let blockBottom = rc.y + rc.height;

		for (let i = 0; i < blocks.length; i++)
		{
			if (i === index)
				continue;

			let blockCompare = blocks[i];
			let top = blockCompare.rect.y;
			let bottom = blockCompare.rect.y + blockCompare.rect.height;

			if ((blockTop >= top && blockTop <= bottom)
				|| (blockBottom >= top && blockBottom <= bottom)
				|| (blockTop <= top && blockBottom >= bottom))
			{
				count++;
			}
		}

		return count;
	}

	private insertRecurringEvents(event: CalendarEvent, events: Array<CalendarEvent>, end: moment.Moment, period: moment.unitOfTime.DurationConstructor)
	{
		let dt = moment(event.start).add(1, period);

		let duration = moment.duration(moment(event.end).diff(moment(event.start)));

		while (dt.isSameOrBefore(end))
		{
			let newEvent = event.clone();
			newEvent.start = moment(dt);
			newEvent.end = moment(dt).add(duration);

			events.push(newEvent);

			dt = moment(dt).add(1, period);
		}
	}
}